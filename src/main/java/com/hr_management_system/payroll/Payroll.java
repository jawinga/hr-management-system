package com.hr_management_system.payroll;


import com.hr_management_system.employee.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDate paymentDate;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal baseSalary;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal grossSalary;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal netSalary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayrollMethodEnum paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayrollStatusEnum paymentStatus;

    @Column(nullable = true)
    private BigDecimal overtimeHours;

    @Column(nullable = true, precision = 19, scale = 2)
    private BigDecimal overtimePay;

    @Column(nullable = true, precision = 19, scale = 2)
    private BigDecimal bonuses;

    @Column(nullable = true, precision = 19, scale = 2)
    private BigDecimal commissions;

    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "100.0", inclusive = true)
    private BigDecimal incomeTax;

    @Column(nullable = false)
    private BigDecimal socialSecurity;

    @Column(nullable = false)
    private BigDecimal healthInsurance;

    @Column(nullable = true, precision = 19, scale = 2)
    private BigDecimal pensionContribution;

    private String processedBy;

    @Column(nullable = true, length = 500)
    private String notes;

    @Column(nullable = false)
    @URL(message = "Please provide a valid URL")
    @NotBlank
    private String payslipDocumentUrl;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
