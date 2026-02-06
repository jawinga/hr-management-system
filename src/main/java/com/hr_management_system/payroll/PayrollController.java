package com.hr_management_system.payroll;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payroll")
@AllArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;

    @PostMapping
    public ResponseEntity<Payroll> addPayroll(@Valid @RequestBody Payroll p) {
        Payroll payroll = payrollService.createPayroll(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(payroll);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayroll(@PathVariable("id") Long id, @Valid @RequestBody Payroll p) {
        if (!id.equals(p.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(p);
        }

        Payroll payroll = payrollService.updatePayroll(p);
        return ResponseEntity.status(HttpStatus.OK).body(payroll);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayroll(@PathVariable("id") Long id) {
        try {
            payrollService.deletePayroll(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPayroll(@PathVariable("id") Long id) {
        try {
            Payroll payroll = payrollService.findPayroll(id);
            return ResponseEntity.status(HttpStatus.OK).body(payroll);
        } catch (EntityNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<List<Payroll>> getAllPayrolls() {
        List<Payroll> list = payrollService.findAllPayrolls();
        return ResponseEntity.ok(list);
    }
}
