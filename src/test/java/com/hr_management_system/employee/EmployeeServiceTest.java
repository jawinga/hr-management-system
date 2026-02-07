package com.hr_management_system.employee;

import com.hr_management_system.department.Department;
import com.hr_management_system.department.DepartmentEnum;
import com.hr_management_system.department.DepartmentRepository;
import com.hr_management_system.position.Position;
import com.hr_management_system.position.PositionRepository;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeRequest employeeRequest;
    private Employee employee;
    private Position position;
    private Department department;

    @BeforeEach
    void setUp() {
        position = new Position();
        position.setId(1L);
        position.setPositionTitle("Software Engineer");
        position.setMinSalary(new BigDecimal("50000"));
        position.setMaxSalary(new BigDecimal("80000"));

        department = new Department();
        department.setId(1L);
        department.setDepartmentName(DepartmentEnum.ENGINEERING);

        employeeRequest = EmployeeRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .companyEmail("john@company.com")
                .email("john@personal.com")
                .phoneNumber("+34 612 345 678")
                .dateOfBirth(LocalDate.of(1990, 5, 15))
                .role(EmployeeRole.EMPLOYEE)
                .salary(new BigDecimal("65000"))
                .positionId(1L)
                .departmentIds(List.of(1L))
                .build();

        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setCompanyEmail("john@company.com");
        employee.setEmail("john@personal.com");
        employee.setRole(EmployeeRole.EMPLOYEE);
        employee.setSalary(new BigDecimal("65000"));
        employee.setPosition(position);
        employee.setDepartments(List.of(department));
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createEmployee_Success() {
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(departmentRepository.findAllById(List.of(1L))).thenReturn(List.of(department));
        when(employeeRepository.existsByCompanyEmail("john@company.com")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee e = invocation.getArgument(0);
            e.setId(1L);
            e.setCreatedAt(LocalDateTime.now());
            e.setUpdatedAt(LocalDateTime.now());
            return e;
        });

        EmployeeResponse response = employeeService.createEmployee(employeeRequest);

        assertNotNull(response);
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void createEmployee_DuplicateEmail_ThrowsException() {
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(departmentRepository.findAllById(List.of(1L))).thenReturn(List.of(department));
        when(employeeRepository.existsByCompanyEmail("john@company.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> employeeService.createEmployee(employeeRequest));
    }

    @Test
    void createEmployee_InvalidSalary_ThrowsException() {
        employeeRequest.setSalary(new BigDecimal("999999"));
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(departmentRepository.findAllById(List.of(1L))).thenReturn(List.of(department));

        assertThrows(IllegalArgumentException.class, () -> employeeService.createEmployee(employeeRequest));
    }

    @Test
    void updateEmployee_Success() {
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(departmentRepository.findAllById(List.of(1L))).thenReturn(List.of(department));
        when(employeeRepository.existsByCompanyEmailAndIdNot("john@company.com", 1L)).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee e = invocation.getArgument(0);
            e.setCreatedAt(LocalDateTime.now());
            e.setUpdatedAt(LocalDateTime.now());
            return e;
        });

        EmployeeResponse response = employeeService.updateEmployee(1L, employeeRequest);

        assertNotNull(response);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void updateEmployee_NotFound() {
        when(employeeRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> employeeService.updateEmployee(1L, employeeRequest));
    }

    @Test
    void deleteEmployee_Success() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void deleteEmployee_NotFound() {
        when(employeeRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> employeeService.deleteEmployee(1L));
    }

    @Test
    void findEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeResponse response = employeeService.findEmployee(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John", response.getFirstName());
    }

    @Test
    void findEmployee_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> employeeService.findEmployee(1L));
    }

    @Test
    void findAllEmployees_ReturnsPaginated() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Employee> page = new PageImpl<>(List.of(employee));
        when(employeeRepository.findAll(pageable)).thenReturn(page);

        Page<EmployeeResponse> result = employeeService.findAllEmployees(pageable);

        assertEquals(1, result.getTotalElements());
    }
}
