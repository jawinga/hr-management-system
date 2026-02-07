package com.hr_management_system.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String companyEmail;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private EmployeeRole role;
    private BigDecimal salary;
    private Long positionId;
    private String positionTitle;
    private List<Long> departmentIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static EmployeeResponse fromEntity(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .companyEmail(employee.getCompanyEmail())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .dateOfBirth(employee.getDateOfBirth())
                .role(employee.getRole())
                .salary(employee.getSalary())
                .positionId(employee.getPosition() != null ? employee.getPosition().getId() : null)
                .positionTitle(employee.getPosition() != null ? employee.getPosition().getPositionTitle() : null)
                .departmentIds(employee.getDepartments() != null
                        ? employee.getDepartments().stream().map(d -> d.getId()).collect(Collectors.toList())
                        : null)
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
}
