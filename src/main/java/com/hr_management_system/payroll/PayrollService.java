package com.hr_management_system.payroll;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PayrollService {

    private final PayrollRepository payrollRepository;

    Payroll createPayroll(Payroll p) {
        return payrollRepository.save(p);
    }

    Payroll updatePayroll(Payroll p) {
        if (p.getId() == null || !payrollRepository.existsById(p.getId())) {
            throw new EntityNotFoundException("Payroll with ID " + p.getId() + " not found");
        }
        return payrollRepository.save(p);
    }

    void deletePayroll(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id not provided");
        }
        if (!payrollRepository.existsById(id)) {
            throw new EntityNotFoundException("Payroll with ID " + id + " not found");
        }
        payrollRepository.deleteById(id);
    }

    Payroll findPayroll(Long id) {
        if (id == null) throw new IllegalArgumentException("Id not provided");

        return payrollRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payroll not found"));
    }

    List<Payroll> findAllPayrolls() {
        return payrollRepository.findAll();
    }
}
