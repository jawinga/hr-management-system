package com.hr_management_system.department;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentService departmentService;

    private ObjectMapper objectMapper;
    private DepartmentRequest departmentRequest;
    private DepartmentResponse departmentResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        departmentRequest = DepartmentRequest.builder()
                .departmentName(DepartmentEnum.ENGINEERING)
                .active(true)
                .contactDept("engineering@company.com")
                .build();

        departmentResponse = DepartmentResponse.builder()
                .id(1L)
                .departmentName(DepartmentEnum.ENGINEERING)
                .departmentDescription("Responsible for software development and technical infrastructure.")
                .active(true)
                .contactDept("engineering@company.com")
                .employeeCount(5)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @WithMockUser
    void createDepartment_Returns201() throws Exception {
        when(departmentService.createDepartment(any(DepartmentRequest.class))).thenReturn(departmentResponse);

        mockMvc.perform(post("/api/v1/departments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.departmentName").value("ENGINEERING"));
    }

    @Test
    @WithMockUser
    void getDepartment_Returns200() throws Exception {
        when(departmentService.findDepartment(1L)).thenReturn(departmentResponse);

        mockMvc.perform(get("/api/v1/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentName").value("ENGINEERING"));
    }

    @Test
    @WithMockUser
    void getDepartment_Returns404() throws Exception {
        when(departmentService.findDepartment(99L)).thenThrow(new EntityNotFoundException("Department not found"));

        mockMvc.perform(get("/api/v1/departments/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void getAllDepartments_ReturnsPaginated() throws Exception {
        Page<DepartmentResponse> page = new PageImpl<>(List.of(departmentResponse));
        when(departmentService.findAllDepartments(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    @WithMockUser
    void updateDepartment_Returns200() throws Exception {
        when(departmentService.updateDepartment(eq(1L), any(DepartmentRequest.class))).thenReturn(departmentResponse);

        mockMvc.perform(put("/api/v1/departments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void deleteDepartment_Returns204() throws Exception {
        doNothing().when(departmentService).deleteDepartment(1L);

        mockMvc.perform(delete("/api/v1/departments/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getDepartment_Returns401_Unauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/departments/1"))
                .andExpect(status().isUnauthorized());
    }
}
