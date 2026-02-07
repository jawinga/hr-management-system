package com.hr_management_system.position;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PositionResponse {

    private Long id;
    private String positionTitle;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PositionResponse fromEntity(Position position) {
        return PositionResponse.builder()
                .id(position.getId())
                .positionTitle(position.getPositionTitle())
                .minSalary(position.getMinSalary())
                .maxSalary(position.getMaxSalary())
                .createdAt(position.getCreatedAt())
                .updatedAt(position.getUpdatedAt())
                .build();
    }
}
