package com.hr_management_system.payroll;

import com.hr_management_system.employee.Employee;
import com.hr_management_system.employee.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayrollServiceTest {

    @Mock
    private PayrollRepository payrollRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private PayrollService payrollService;

    private Employee employee;
    private PayrollRequest payrollRequest;
    private Payroll payroll;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");

        payrollRequest = PayrollRequest.builder()
                .employeeId(1L)
                .paymentDate(LocalDate.of(2024, 1, 31))
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 1, 31))
                .baseSalary(new BigDecimal("5000"))
                .paymentMethod(PayrollMethodEnum.DEPOSIT)
                .paymentStatus(PayrollStatusEnum.PENDING)
                .overtimePay(new BigDecimal("500"))
                .bonuses(new BigDecimal("200"))
                .commissions(new BigDecimal("100"))
                .incomeTax(new BigDecimal("20"))
                .socialSecurity(new BigDecimal("300"))
                .healthInsurance(new BigDecimal("150"))
                .pensionContribution(new BigDecimal("100"))
                .payslipDocumentUrl("https://example.com/payslip.pdf")
                .build();

        payroll = new Payroll();
        payroll.setId(1L);
        payroll.setEmployee(employee);
        payroll.setPaymentDate(LocalDate.of(2024, 1, 31));
        payroll.setStartDate(LocalDate.of(2024, 1, 1));
        payroll.setEndDate(LocalDate.of(2024, 1, 31));
        payroll.setBaseSalary(new BigDecimal("5000"));
        payroll.setGrossSalary(new BigDecimal("5800"));
        payroll.setNetSalary(new BigDecimal("4090.00"));
        payroll.setPaymentMethod(PayrollMethodEnum.DEPOSIT);
        payroll.setPaymentStatus(PayrollStatusEnum.PENDING);
        payroll.setOvertimePay(new BigDecimal("500"));
        payroll.setBonuses(new BigDecimal("200"));
        payroll.setCommissions(new BigDecimal("100"));
        payroll.setIncomeTax(new BigDecimal("20"));
        payroll.setSocialSecurity(new BigDecimal("300"));
        payroll.setHealthInsurance(new BigDecimal("150"));
        payroll.setPensionContribution(new BigDecimal("100"));
        payroll.setPayslipDocumentUrl("https://example.com/payslip.pdf");
        payroll.setCreatedAt(LocalDateTime.now());
        payroll.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createPayroll_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(payrollRepository.save(any(Payroll.class))).thenAnswer(invocation -> {
            Payroll p = invocation.getArgument(0);
            p.setId(1L);
            p.setCreatedAt(LocalDateTime.now());
            p.setUpdatedAt(LocalDateTime.now());
            return p;
        });

        PayrollResponse response = payrollService.createPayroll(payrollRequest);

        assertNotNull(response);
        assertEquals(1L, response.getEmployeeId());
        assertEquals("John Doe", response.getEmployeeName());
        verify(payrollRepository).save(any(Payroll.class));
    }

    @Test
    void createPayroll_CalculatesGrossSalaryCorrectly() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(payrollRepository.save(any(Payroll.class))).thenAnswer(invocation -> {
            Payroll p = invocation.getArgument(0);
            p.setId(1L);
            p.setCreatedAt(LocalDateTime.now());
            p.setUpdatedAt(LocalDateTime.now());
            return p;
        });

        PayrollResponse response = payrollService.createPayroll(payrollRequest);

        // grossSalary = 5000 + 500 + 200 + 100 = 5800
        assertEquals(new BigDecimal("5800"), response.getGrossSalary());
    }

    @Test
    void createPayroll_CalculatesNetSalaryCorrectly() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(payrollRepository.save(any(Payroll.class))).thenAnswer(invocation -> {
            Payroll p = invocation.getArgument(0);
            p.setId(1L);
            p.setCreatedAt(LocalDateTime.now());
            p.setUpdatedAt(LocalDateTime.now());
            return p;
        });

        PayrollResponse response = payrollService.createPayroll(payrollRequest);

        // netSalary = 5800 - (5800 * 20/100) - 300 - 150 - 100 = 5800 - 1160 - 550 = 4090
        assertEquals(new BigDecimal("4090.00"), response.getNetSalary());
    }

    @Test
    void createPayroll_EmployeeNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> payrollService.createPayroll(payrollRequest));
    }

    @Test
    void updatePayroll_Success() {
        when(payrollRepository.existsById(1L)).thenReturn(true);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(payrollRepository.save(any(Payroll.class))).thenAnswer(invocation -> {
            Payroll p = invocation.getArgument(0);
            p.setCreatedAt(LocalDateTime.now());
            p.setUpdatedAt(LocalDateTime.now());
            return p;
        });

        PayrollResponse response = payrollService.updatePayroll(1L, payrollRequest);

        assertNotNull(response);
        verify(payrollRepository).save(any(Payroll.class));
    }

    @Test
    void updatePayroll_NotFound() {
        when(payrollRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> payrollService.updatePayroll(1L, payrollRequest));
    }

    @Test
    void deletePayroll_Success() {
        when(payrollRepository.existsById(1L)).thenReturn(true);

        payrollService.deletePayroll(1L);

        verify(payrollRepository).deleteById(1L);
    }

    @Test
    void deletePayroll_NotFound() {
        when(payrollRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> payrollService.deletePayroll(1L));
    }

    @Test
    void findPayroll_Success() {
        when(payrollRepository.findById(1L)).thenReturn(Optional.of(payroll));

        PayrollResponse response = payrollService.findPayroll(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void findPayroll_NotFound() {
        when(payrollRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> payrollService.findPayroll(1L));
    }

    @Test
    void findAllPayrolls_Success() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Payroll> page = new PageImpl<>(List.of(payroll));
        when(payrollRepository.findAll(pageable)).thenReturn(page);

        Page<PayrollResponse> result = payrollService.findAllPayrolls(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByEmployeeId_Success() {
        when(payrollRepository.findByEmployeeId(1L)).thenReturn(List.of(payroll));

        List<PayrollResponse> result = payrollService.findByEmployeeId(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getEmployeeId());
    }

    @Test
    void findByPaymentStatus_Success() {
        when(payrollRepository.findByPaymentStatus(PayrollStatusEnum.PENDING)).thenReturn(List.of(payroll));

        List<PayrollResponse> result = payrollService.findByPaymentStatus(PayrollStatusEnum.PENDING);

        assertEquals(1, result.size());
        assertEquals(PayrollStatusEnum.PENDING, result.get(0).getPaymentStatus());
    }
}
