package com.hr_management_system.employee;

import com.hr_management_system.department.Department;
import com.hr_management_system.department.DepartmentRepository;
import com.hr_management_system.position.Position;
import com.hr_management_system.position.PositionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           PositionRepository positionRepository,
                           DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
    }

    EmployeeResponse createEmployee(EmployeeRequest request) {
        Employee employee = mapRequestToEntity(request);
        validateEmployee(employee);

        Employee saved = employeeRepository.save(employee);
        log.info("Created employee with ID {}", saved.getId());
        return EmployeeResponse.fromEntity(saved);
    }

    EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Employee with ID " + id + " not found");
        }

        Employee employee = mapRequestToEntity(request);
        employee.setId(id);
        validateEmployee(employee);

        Employee saved = employeeRepository.save(employee);
        log.info("Updated employee with ID {}", saved.getId());
        return EmployeeResponse.fromEntity(saved);
    }

    void deleteEmployee(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Could not find Id of user");
        }
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Employee with ID " + id + " not found");
        }
        employeeRepository.deleteById(id);
        log.info("Deleted employee with ID {}", id);
    }

    EmployeeResponse findEmployee(Long id) {
        if (id == null) throw new IllegalArgumentException("Id not provided");

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return EmployeeResponse.fromEntity(employee);
    }

    Page<EmployeeResponse> findAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(EmployeeResponse::fromEntity);
    }

    private void validateEmployee(Employee e) {
        if (!e.isValidSalary()) {
            throw new IllegalArgumentException("Salary must be between position's min and max range");
        }

        if (e.getFirstName() == null || e.getFirstName().isEmpty()
                || e.getLastName() == null || e.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Employee must have first and last name");
        }

        if (e.getCompanyEmail() == null || e.getCompanyEmail().isEmpty()) {
            throw new IllegalArgumentException("Employee must have company email");
        }

        if (!companyEmailIsUnique(e.getCompanyEmail(), e.getId())) {
            throw new IllegalArgumentException("Please use a unique email");
        }
    }

    private boolean companyEmailIsUnique(String email, Long excludeId) {
        if (excludeId == null) {
            return !employeeRepository.existsByCompanyEmail(email);
        } else {
            return !employeeRepository.existsByCompanyEmailAndIdNot(email, excludeId);
        }
    }

    private Employee mapRequestToEntity(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setCompanyEmail(request.getCompanyEmail());
        employee.setEmail(request.getEmail());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setRole(request.getRole());
        employee.setSocialSecurity(request.getSocialSecurity());
        employee.setBankDetails(request.getBankDetails());
        employee.setSalary(request.getSalary());

        if (request.getPositionId() != null) {
            Position position = positionRepository.findById(request.getPositionId())
                    .orElseThrow(() -> new EntityNotFoundException("Position with ID " + request.getPositionId() + " not found"));
            employee.setPosition(position);
        }

        if (request.getDepartmentIds() != null && !request.getDepartmentIds().isEmpty()) {
            List<Department> departments = departmentRepository.findAllById(request.getDepartmentIds());
            if (departments.size() != request.getDepartmentIds().size()) {
                throw new EntityNotFoundException("One or more departments not found");
            }
            employee.setDepartments(departments);
        }

        return employee;
    }
}
