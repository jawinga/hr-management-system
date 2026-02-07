package com.hr_management_system.payroll;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayrollRequest {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Base salary is required")
    private BigDecimal baseSalary;

    @NotNull(message = "Payment method is required")
    private PayrollMethodEnum paymentMethod;

    @NotNull(message = "Payment status is required")
    private PayrollStatusEnum paymentStatus;

    private BigDecimal overtimeHours;
    private BigDecimal overtimePay;
    private BigDecimal bonuses;
    private BigDecimal commissions;

    @NotNull(message = "Income tax is required")
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "100.0", inclusive = true)
    private BigDecimal incomeTax;

    @NotNull(message = "Social security is required")
    private BigDecimal socialSecurity;

    @NotNull(message = "Health insurance is required")
    private BigDecimal healthInsurance;

    private BigDecimal pensionContribution;

    private String processedBy;

    private String notes;

    @NotBlank(message = "Payslip document URL is required")
    @URL(message = "Please provide a valid URL")
    private String payslipDocumentUrl;
}
