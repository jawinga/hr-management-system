package com.hr_management_system.department;

import com.hr_management_system.employee.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private DepartmentEnum departmentName;
    private boolean active;
    @Email
    private String contactDept;
    @ManyToOne
    private Employee head;
    @ManyToMany(mappedBy = "departments")
    private List<Employee> employees;
    public String getDepartmentDescription() {
        return departmentName != null ? departmentName.getDescription() : null;
    }

}
