package com.hr_management_system.payroll;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PayrollController.class)
class PayrollControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PayrollService payrollService;

    private ObjectMapper objectMapper;
    private PayrollRequest payrollRequest;
    private PayrollResponse payrollResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        payrollRequest = PayrollRequest.builder()
                .employeeId(1L)
                .paymentDate(LocalDate.of(2024, 1, 31))
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 1, 31))
                .baseSalary(new BigDecimal("5000"))
                .paymentMethod(PayrollMethodEnum.DEPOSIT)
                .paymentStatus(PayrollStatusEnum.PENDING)
                .incomeTax(new BigDecimal("20"))
                .socialSecurity(new BigDecimal("300"))
                .healthInsurance(new BigDecimal("150"))
                .payslipDocumentUrl("https://example.com/payslip.pdf")
                .build();

        payrollResponse = PayrollResponse.builder()
                .id(1L)
                .employeeId(1L)
                .employeeName("John Doe")
                .paymentDate(LocalDate.of(2024, 1, 31))
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 1, 31))
                .baseSalary(new BigDecimal("5000"))
                .grossSalary(new BigDecimal("5000"))
                .netSalary(new BigDecimal("3550.00"))
                .paymentMethod(PayrollMethodEnum.DEPOSIT)
                .paymentStatus(PayrollStatusEnum.PENDING)
                .incomeTax(new BigDecimal("20"))
                .socialSecurity(new BigDecimal("300"))
                .healthInsurance(new BigDecimal("150"))
                .payslipDocumentUrl("https://example.com/payslip.pdf")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @WithMockUser
    void createPayroll_Returns201() throws Exception {
        when(payrollService.createPayroll(any(PayrollRequest.class))).thenReturn(payrollResponse);

        mockMvc.perform(post("/api/v1/payroll")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payrollRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.employeeName").value("John Doe"));
    }

    @Test
    @WithMockUser
    void createPayroll_Returns400_ValidationError() throws Exception {
        PayrollRequest invalid = PayrollRequest.builder().build();

        mockMvc.perform(post("/api/v1/payroll")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void updatePayroll_Returns200() throws Exception {
        when(payrollService.updatePayroll(eq(1L), any(PayrollRequest.class))).thenReturn(payrollResponse);

        mockMvc.perform(put("/api/v1/payroll/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payrollRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void deletePayroll_Returns204() throws Exception {
        doNothing().when(payrollService).deletePayroll(1L);

        mockMvc.perform(delete("/api/v1/payroll/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void getPayroll_Returns200() throws Exception {
        when(payrollService.findPayroll(1L)).thenReturn(payrollResponse);

        mockMvc.perform(get("/api/v1/payroll/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.employeeName").value("John Doe"));
    }

    @Test
    @WithMockUser
    void getPayroll_Returns404() throws Exception {
        when(payrollService.findPayroll(99L)).thenThrow(new EntityNotFoundException("Payroll not found"));

        mockMvc.perform(get("/api/v1/payroll/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void getAllPayrolls_ReturnsPaginatedResults() throws Exception {
        Page<PayrollResponse> page = new PageImpl<>(List.of(payrollResponse));
        when(payrollService.findAllPayrolls(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/payroll")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser
    void getPayrollsByEmployee_Returns200() throws Exception {
        when(payrollService.findByEmployeeId(1L)).thenReturn(List.of(payrollResponse));

        mockMvc.perform(get("/api/v1/payroll/employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeId").value(1));
    }

    @Test
    @WithMockUser
    void getPayrollsByStatus_Returns200() throws Exception {
        when(payrollService.findByPaymentStatus(PayrollStatusEnum.PENDING)).thenReturn(List.of(payrollResponse));

        mockMvc.perform(get("/api/v1/payroll/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].paymentStatus").value("PENDING"));
    }

    @Test
    void getPayroll_Returns401_Unauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/payroll/1"))
                .andExpect(status().isUnauthorized());
    }
}
