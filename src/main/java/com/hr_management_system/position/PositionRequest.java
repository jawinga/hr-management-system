package com.hr_management_system.position;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PositionRequest {

    @NotBlank(message = "Position title is required")
    private String positionTitle;

    @NotNull(message = "Minimum salary is required")
    private BigDecimal minSalary;

    @NotNull(message = "Maximum salary is required")
    private BigDecimal maxSalary;
}
