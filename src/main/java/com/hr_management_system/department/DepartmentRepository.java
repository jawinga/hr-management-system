package com.hr_management_system.department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {


    boolean existsByDepartmentName(DepartmentEnum name);

}
