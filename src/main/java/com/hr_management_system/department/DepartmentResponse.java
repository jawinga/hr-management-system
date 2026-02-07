package com.hr_management_system.department;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponse {

    private Long id;
    private DepartmentEnum departmentName;
    private String departmentDescription;
    private boolean active;
    private String contactDept;
    private String headEmployeeName;
    private int employeeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static DepartmentResponse fromEntity(Department department) {
        String headName = null;
        if (department.getHead() != null) {
            headName = department.getHead().getFirstName() + " " + department.getHead().getLastName();
        }

        int empCount = department.getEmployees() != null ? department.getEmployees().size() : 0;

        return DepartmentResponse.builder()
                .id(department.getId())
                .departmentName(department.getDepartmentName())
                .departmentDescription(department.getDepartmentDescription())
                .active(department.isActive())
                .contactDept(department.getContactDept())
                .headEmployeeName(headName)
                .employeeCount(empCount)
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }
}
