package com.hr_management_system.employee;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;


    public Employee createEmployee(Employee e){

        if(!e.isValidSalary()) {
            throw new IllegalArgumentException("Salary must be between position's min and max range");
        }

        if((e.getFirstName() == null || e.getFirstName().isEmpty() || e.getLastName() == null || e.getLastName().isEmpty())){
            throw new IllegalArgumentException("Employee must have first and last name");
        }

        if(e.getEmail().isEmpty()){
            throw new IllegalArgumentException("Employee must have company email");
        }

        if(!companyEmailIsUnique(e.getCompanyEmail())) {
            throw new IllegalArgumentException("Please use a unique email");

        }

        employeeRepository.save(e);
        System.out.println(e);
        return  e;

    }

    public void deleteEmployee(Long id){

        if(id == null){
            throw new IllegalArgumentException("Could not find Id of user");
        }

        if (!employeeRepository.existsById(id)){
            throw new EntityNotFoundException("Employee with ID " + id + " not found");
        }

        employeeRepository.deleteById(id);

    }


    boolean companyEmailIsUnique(String email){

        return !employeeRepository.existsByCompanyEmail(email);
    }





}
