package com.hr_management_system.payroll;

import com.hr_management_system.employee.Employee;
import com.hr_management_system.employee.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayrollService {

    private static final Logger log = LoggerFactory.getLogger(PayrollService.class);

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;

    public PayrollService(PayrollRepository payrollRepository, EmployeeRepository employeeRepository) {
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
    }

    PayrollResponse createPayroll(PayrollRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee with ID " + request.getEmployeeId() + " not found"));

        Payroll payroll = mapRequestToEntity(request, employee);
        calculateSalary(payroll);

        Payroll saved = payrollRepository.save(payroll);
        log.info("Created payroll with ID {} for employee {}", saved.getId(), employee.getId());
        return PayrollResponse.fromEntity(saved);
    }

    PayrollResponse updatePayroll(Long id, PayrollRequest request) {
        if (!payrollRepository.existsById(id)) {
            throw new EntityNotFoundException("Payroll with ID " + id + " not found");
        }

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee with ID " + request.getEmployeeId() + " not found"));

        Payroll payroll = mapRequestToEntity(request, employee);
        payroll.setId(id);
        calculateSalary(payroll);

        Payroll saved = payrollRepository.save(payroll);
        log.info("Updated payroll with ID {}", saved.getId());
        return PayrollResponse.fromEntity(saved);
    }

    void deletePayroll(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id not provided");
        }
        if (!payrollRepository.existsById(id)) {
            throw new EntityNotFoundException("Payroll with ID " + id + " not found");
        }
        payrollRepository.deleteById(id);
        log.info("Deleted payroll with ID {}", id);
    }

    PayrollResponse findPayroll(Long id) {
        if (id == null) throw new IllegalArgumentException("Id not provided");

        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payroll not found"));
        return PayrollResponse.fromEntity(payroll);
    }

    Page<PayrollResponse> findAllPayrolls(Pageable pageable) {
        return payrollRepository.findAll(pageable).map(PayrollResponse::fromEntity);
    }

    List<PayrollResponse> findByEmployeeId(Long employeeId) {
        return payrollRepository.findByEmployeeId(employeeId).stream()
                .map(PayrollResponse::fromEntity)
                .collect(Collectors.toList());
    }

    List<PayrollResponse> findByPaymentStatus(PayrollStatusEnum status) {
        return payrollRepository.findByPaymentStatus(status).stream()
                .map(PayrollResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private void calculateSalary(Payroll payroll) {
        BigDecimal overtimePay = payroll.getOvertimePay() != null ? payroll.getOvertimePay() : BigDecimal.ZERO;
        BigDecimal bonuses = payroll.getBonuses() != null ? payroll.getBonuses() : BigDecimal.ZERO;
        BigDecimal commissions = payroll.getCommissions() != null ? payroll.getCommissions() : BigDecimal.ZERO;

        BigDecimal grossSalary = payroll.getBaseSalary()
                .add(overtimePay)
                .add(bonuses)
                .add(commissions);
        payroll.setGrossSalary(grossSalary);

        BigDecimal pensionContribution = payroll.getPensionContribution() != null ? payroll.getPensionContribution() : BigDecimal.ZERO;

        BigDecimal incomeTaxAmount = grossSalary.multiply(payroll.getIncomeTax())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        BigDecimal netSalary = grossSalary
                .subtract(incomeTaxAmount)
                .subtract(payroll.getSocialSecurity())
                .subtract(payroll.getHealthInsurance())
                .subtract(pensionContribution);
        payroll.setNetSalary(netSalary);
    }

    private Payroll mapRequestToEntity(PayrollRequest request, Employee employee) {
        Payroll payroll = new Payroll();
        payroll.setEmployee(employee);
        payroll.setPaymentDate(request.getPaymentDate());
        payroll.setStartDate(request.getStartDate());
        payroll.setEndDate(request.getEndDate());
        payroll.setBaseSalary(request.getBaseSalary());
        payroll.setPaymentMethod(request.getPaymentMethod());
        payroll.setPaymentStatus(request.getPaymentStatus());
        payroll.setOvertimeHours(request.getOvertimeHours());
        payroll.setOvertimePay(request.getOvertimePay());
        payroll.setBonuses(request.getBonuses());
        payroll.setCommissions(request.getCommissions());
        payroll.setIncomeTax(request.getIncomeTax());
        payroll.setSocialSecurity(request.getSocialSecurity());
        payroll.setHealthInsurance(request.getHealthInsurance());
        payroll.setPensionContribution(request.getPensionContribution());
        payroll.setProcessedBy(request.getProcessedBy());
        payroll.setNotes(request.getNotes());
        payroll.setPayslipDocumentUrl(request.getPayslipDocumentUrl());
        return payroll;
    }
}
