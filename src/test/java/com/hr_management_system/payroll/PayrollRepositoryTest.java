package com.hr_management_system.payroll;

import com.hr_management_system.employee.Employee;
import com.hr_management_system.employee.EmployeeRepository;
import com.hr_management_system.employee.EmployeeRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PayrollRepositoryTest {

    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        payrollRepository.deleteAll();
        employeeRepository.deleteAll();

        employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setCompanyEmail("john@company.com");
        employee.setEmail("john@personal.com");
        employee.setRole(EmployeeRole.EMPLOYEE);
        employee.setSalary(new BigDecimal("65000"));
        employee = employeeRepository.save(employee);

        Payroll payroll1 = new Payroll();
        payroll1.setEmployee(employee);
        payroll1.setPaymentDate(LocalDate.of(2024, 1, 31));
        payroll1.setStartDate(LocalDate.of(2024, 1, 1));
        payroll1.setEndDate(LocalDate.of(2024, 1, 31));
        payroll1.setBaseSalary(new BigDecimal("5000"));
        payroll1.setGrossSalary(new BigDecimal("5000"));
        payroll1.setNetSalary(new BigDecimal("4000"));
        payroll1.setPaymentMethod(PayrollMethodEnum.DEPOSIT);
        payroll1.setPaymentStatus(PayrollStatusEnum.COMPLETE);
        payroll1.setIncomeTax(new BigDecimal("15"));
        payroll1.setSocialSecurity(new BigDecimal("200"));
        payroll1.setHealthInsurance(new BigDecimal("100"));
        payroll1.setPayslipDocumentUrl("https://example.com/payslip1.pdf");
        payrollRepository.save(payroll1);

        Payroll payroll2 = new Payroll();
        payroll2.setEmployee(employee);
        payroll2.setPaymentDate(LocalDate.of(2024, 2, 29));
        payroll2.setStartDate(LocalDate.of(2024, 2, 1));
        payroll2.setEndDate(LocalDate.of(2024, 2, 29));
        payroll2.setBaseSalary(new BigDecimal("5000"));
        payroll2.setGrossSalary(new BigDecimal("5000"));
        payroll2.setNetSalary(new BigDecimal("4000"));
        payroll2.setPaymentMethod(PayrollMethodEnum.DEPOSIT);
        payroll2.setPaymentStatus(PayrollStatusEnum.PENDING);
        payroll2.setIncomeTax(new BigDecimal("15"));
        payroll2.setSocialSecurity(new BigDecimal("200"));
        payroll2.setHealthInsurance(new BigDecimal("100"));
        payroll2.setPayslipDocumentUrl("https://example.com/payslip2.pdf");
        payrollRepository.save(payroll2);
    }

    @Test
    void findByEmployeeId_ReturnsPayrolls() {
        List<Payroll> result = payrollRepository.findByEmployeeId(employee.getId());

        assertEquals(2, result.size());
    }

    @Test
    void findByEmployeeId_ReturnsEmpty_WhenNoMatch() {
        List<Payroll> result = payrollRepository.findByEmployeeId(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByPaymentStatus_ReturnsMatchingPayrolls() {
        List<Payroll> completed = payrollRepository.findByPaymentStatus(PayrollStatusEnum.COMPLETE);
        List<Payroll> pending = payrollRepository.findByPaymentStatus(PayrollStatusEnum.PENDING);

        assertEquals(1, completed.size());
        assertEquals(1, pending.size());
    }

    @Test
    void findByPaymentStatus_ReturnsEmpty_WhenNoMatch() {
        List<Payroll> result = payrollRepository.findByPaymentStatus(PayrollStatusEnum.FAILED);

        assertTrue(result.isEmpty());
    }
}
