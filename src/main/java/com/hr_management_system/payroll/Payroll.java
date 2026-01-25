package com.hr_management_system.payroll;


import com.hr_management_system.employee.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDate paymentDate;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

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

    private BigDecimal overtimeHours;
    private BigDecimal overtimePay;
    private BigDecimal bonuses;
    private BigDecimal commissions;

    private BigDecimal incomeTax;
    private BigDecimal socialSecurity;
    private BigDecimal healthInsurance;
    private BigDecimal pensionContribution;
    private BigDecimal otherDeductions;

    private String processedBy;

    @Column(length = 500)
    private String notes;  // For any special remarks

    private String payslipDocumentUrl;  // Link to generated payslip PDF


    /*@AssertTrue(message = "Payment method is not valid")
    public boolean isValidPaymentMethod(){
        return paymentMethod.equals("deposit") || paymentMethod.equals("check") ||
                paymentMethod.equals("cash") || paymentMethod.equals("paypal") ||
                paymentMethod.equals("wire") || paymentMethod.equals("other");
    }

    @AssertTrue(message = "Payment status is not valid")
    public boolean isPaymentStatusValid(){
        return paymentStatus.equals("pending") || paymentStatus.equals("ongoing") ||
                paymentStatus.equals("complete") || paymentStatus.equals("failed");
    }

*/










}
