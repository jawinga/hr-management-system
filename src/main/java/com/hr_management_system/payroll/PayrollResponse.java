package com.hr_management_system.payroll;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayrollResponse {

    private Long id;
    private Long employeeId;
    private String employeeName;
    private LocalDate paymentDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal baseSalary;
    private BigDecimal grossSalary;
    private BigDecimal netSalary;
    private PayrollMethodEnum paymentMethod;
    private PayrollStatusEnum paymentStatus;
    private BigDecimal overtimeHours;
    private BigDecimal overtimePay;
    private BigDecimal bonuses;
    private BigDecimal commissions;
    private BigDecimal incomeTax;
    private BigDecimal socialSecurity;
    private BigDecimal healthInsurance;
    private BigDecimal pensionContribution;
    private String processedBy;
    private String notes;
    private String payslipDocumentUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PayrollResponse fromEntity(Payroll payroll) {
        String empName = null;
        if (payroll.getEmployee() != null) {
            empName = payroll.getEmployee().getFirstName() + " " + payroll.getEmployee().getLastName();
        }

        return PayrollResponse.builder()
                .id(payroll.getId())
                .employeeId(payroll.getEmployee() != null ? payroll.getEmployee().getId() : null)
                .employeeName(empName)
                .paymentDate(payroll.getPaymentDate())
                .startDate(payroll.getStartDate())
                .endDate(payroll.getEndDate())
                .baseSalary(payroll.getBaseSalary())
                .grossSalary(payroll.getGrossSalary())
                .netSalary(payroll.getNetSalary())
                .paymentMethod(payroll.getPaymentMethod())
                .paymentStatus(payroll.getPaymentStatus())
                .overtimeHours(payroll.getOvertimeHours())
                .overtimePay(payroll.getOvertimePay())
                .bonuses(payroll.getBonuses())
                .commissions(payroll.getCommissions())
                .incomeTax(payroll.getIncomeTax())
                .socialSecurity(payroll.getSocialSecurity())
                .healthInsurance(payroll.getHealthInsurance())
                .pensionContribution(payroll.getPensionContribution())
                .processedBy(payroll.getProcessedBy())
                .notes(payroll.getNotes())
                .payslipDocumentUrl(payroll.getPayslipDocumentUrl())
                .createdAt(payroll.getCreatedAt())
                .updatedAt(payroll.getUpdatedAt())
                .build();
    }
}
