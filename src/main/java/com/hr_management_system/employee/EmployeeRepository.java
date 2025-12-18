package com.hr_management_system.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByCompanyEmail(String email);
    boolean existsByCompanyEmailAndIdNot(String email, Long id);




}
