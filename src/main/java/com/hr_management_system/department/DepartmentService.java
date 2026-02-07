package com.hr_management_system.department;

import com.hr_management_system.employee.Employee;
import com.hr_management_system.employee.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentService(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByDepartmentName(request.getDepartmentName())) {
            throw new RuntimeException("Department already exists!");
        }

        Department department = mapRequestToEntity(request);
        Department saved = departmentRepository.save(department);
        log.info("Created department with ID {}", saved.getId());
        return DepartmentResponse.fromEntity(saved);
    }

    DepartmentResponse findDepartment(Long id) {
        if (id == null) throw new IllegalArgumentException("Id not provided");

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));
        return DepartmentResponse.fromEntity(department);
    }

    Page<DepartmentResponse> findAllDepartments(Pageable pageable) {
        return departmentRepository.findAll(pageable).map(DepartmentResponse::fromEntity);
    }

    void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Department could not be found!");
        }
        departmentRepository.deleteById(id);
        log.info("Deleted department with ID {}", id);
    }

    DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        if (!departmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Department with ID " + id + " not found");
        }

        Department department = mapRequestToEntity(request);
        department.setId(id);

        Department saved = departmentRepository.save(department);
        log.info("Updated department with ID {}", saved.getId());
        return DepartmentResponse.fromEntity(saved);
    }

    private Department mapRequestToEntity(DepartmentRequest request) {
        Department department = new Department();
        department.setDepartmentName(request.getDepartmentName());
        department.setActive(request.isActive());
        department.setContactDept(request.getContactDept());

        if (request.getHeadEmployeeId() != null) {
            Employee head = employeeRepository.findById(request.getHeadEmployeeId())
                    .orElseThrow(() -> new EntityNotFoundException("Employee with ID " + request.getHeadEmployeeId() + " not found"));
            department.setHead(head);
        }

        return department;
    }
}
