package com.hr_management_system.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;


    Employee createEmployee(Employee e){

        if(!e.isValidSalary()) {
            throw new IllegalArgumentException("Salary must be between position's min and max range");
        }

        if((e.getFirstName() == null || e.getFirstName().isEmpty() || e.getLastName() == null || e.getLastName().isEmpty())){
            throw new IllegalArgumentException("Employee must have first and last name");
        }

        if(e.getEmail().isEmpty()){
            throw new IllegalArgumentException("Employee must have email");
        }

        if(!companyEmailIsUnique(e.getCompanyEmail())) {
            throw new IllegalArgumentException("Please use a unique email");

        }

        employeeRepository.save(e);
        return  e;

    }


    boolean companyEmailIsUnique(String email){

        return !employeeRepository.existsByCompanyEmail(email);
    }



}
