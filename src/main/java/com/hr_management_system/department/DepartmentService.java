package com.hr_management_system.department;
import com.hr_management_system.employee.Employee;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private final DepartmentRepository departmentRepository;


    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    Department createDepartment(Department department){

        if(departmentRepository.existsByDepartmentName(department.getDepartmentName())){
            throw new RuntimeException("Department already exists!");
        }
        System.out.println(department);
        return departmentRepository.save(department);

    }

    Department findDepartment(Long id){
        if(id == null) throw new IllegalArgumentException("Id not provided");

        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    List<Department> findAllDepartments(){

        return departmentRepository.findAll();

    }


    void deleteDepartment(Long id){

        if(!departmentRepository.existsById(id)){
            throw new RuntimeException("Department could not be found!");

        }

        departmentRepository.deleteById(id);


    }


    Department updateDepartment(Department department){

        if(!departmentRepository.existsById(department.getId())){
            throw new EntityNotFoundException("Department with ID " + department.getId() + " not found");

        }

        return departmentRepository.save(department);

    }




}
