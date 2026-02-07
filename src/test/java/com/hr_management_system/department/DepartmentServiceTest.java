package com.hr_management_system.department;

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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private DepartmentRequest departmentRequest;
    private Department department;

    @BeforeEach
    void setUp() {
        departmentRequest = DepartmentRequest.builder()
                .departmentName(DepartmentEnum.ENGINEERING)
                .active(true)
                .contactDept("engineering@company.com")
                .build();

        department = new Department();
        department.setId(1L);
        department.setDepartmentName(DepartmentEnum.ENGINEERING);
        department.setActive(true);
        department.setContactDept("engineering@company.com");
        department.setEmployees(Collections.emptyList());
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createDepartment_Success() {
        when(departmentRepository.existsByDepartmentName(DepartmentEnum.ENGINEERING)).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenAnswer(invocation -> {
            Department d = invocation.getArgument(0);
            d.setId(1L);
            d.setEmployees(Collections.emptyList());
            d.setCreatedAt(LocalDateTime.now());
            d.setUpdatedAt(LocalDateTime.now());
            return d;
        });

        DepartmentResponse response = departmentService.createDepartment(departmentRequest);

        assertNotNull(response);
        assertEquals(DepartmentEnum.ENGINEERING, response.getDepartmentName());
        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    void createDepartment_AlreadyExists_ThrowsException() {
        when(departmentRepository.existsByDepartmentName(DepartmentEnum.ENGINEERING)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> departmentService.createDepartment(departmentRequest));
    }

    @Test
    void findDepartment_Success() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        DepartmentResponse response = departmentService.findDepartment(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void findDepartment_NotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> departmentService.findDepartment(1L));
    }

    @Test
    void findAllDepartments_ReturnsPaginated() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Department> page = new PageImpl<>(List.of(department));
        when(departmentRepository.findAll(pageable)).thenReturn(page);

        Page<DepartmentResponse> result = departmentService.findAllDepartments(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void deleteDepartment_Success() {
        when(departmentRepository.existsById(1L)).thenReturn(true);

        departmentService.deleteDepartment(1L);

        verify(departmentRepository).deleteById(1L);
    }

    @Test
    void deleteDepartment_NotFound() {
        when(departmentRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> departmentService.deleteDepartment(1L));
    }

    @Test
    void updateDepartment_Success() {
        when(departmentRepository.existsById(1L)).thenReturn(true);
        when(departmentRepository.save(any(Department.class))).thenAnswer(invocation -> {
            Department d = invocation.getArgument(0);
            d.setEmployees(Collections.emptyList());
            d.setCreatedAt(LocalDateTime.now());
            d.setUpdatedAt(LocalDateTime.now());
            return d;
        });

        DepartmentResponse response = departmentService.updateDepartment(1L, departmentRequest);

        assertNotNull(response);
        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    void updateDepartment_NotFound() {
        when(departmentRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> departmentService.updateDepartment(1L, departmentRequest));
    }
}
