package com.hr_management_system.employee;

import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    Employee createEmployee(Employee e){

        validateEmployee(e);
        Employee saved = employeeRepository.save(e);
        System.out.println(saved);
        return saved;

    }

     Employee updateEmployee(Employee e){

        if(!employeeRepository.existsById(e.getId()) || e.getId() == null){

            throw new EntityNotFoundException("Employee with ID " + e.getId() + " not found");

        }

        validateEmployee(e);
        return employeeRepository.save(e);

    }


     void deleteEmployee(Long id){

        if(id == null){
            throw new IllegalArgumentException("Could not find Id of user");
        }

        if (!employeeRepository.existsById(id)){
            throw new EntityNotFoundException("Employee with ID " + id + " not found");
        }

        employeeRepository.deleteById(id);

    }

     Employee findEmployee(Long id){
        if(id == null) throw new IllegalArgumentException("Id not provided");

        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

     List<Employee> findAllEmployees() {

        return employeeRepository.findAll();
    }

    void validateEmployee(Employee e){

        if(!e.isValidSalary()) {
            throw new IllegalArgumentException("Salary must be between position's min and max range");
        }

        if((e.getFirstName() == null || e.getFirstName().isEmpty() || e.getLastName() == null || e.getLastName().isEmpty())){
            throw new IllegalArgumentException("Employee must have first and last name");
        }

        if(e.getEmail().isEmpty()){
            throw new IllegalArgumentException("Employee must have company email");
        }

        if(!companyEmailIsUnique(e.getCompanyEmail(), e.getId())){
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




}
