package com.hr_management_system.department;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequest {

    @NotNull(message = "Department name is required")
    private DepartmentEnum departmentName;

    private boolean active;

    @Email(message = "Invalid contact email format")
    private String contactDept;

    private Long headEmployeeId;
}
