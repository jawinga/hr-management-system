package com.hr_management_system.payroll;


import com.hr_management_system.employee.Employee;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Payroll {

    @Id
    private Long id;
    @ManyToOne
    private Employee employee;

    @DateTimeFormat
    private LocalDate paymentDate;










}
