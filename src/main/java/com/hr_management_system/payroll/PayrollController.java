package com.hr_management_system.payroll;


import com.hr_management_system.employee.Employee;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payroll")
@AllArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;

    @PostMapping
    public ResponseEntity<Payroll> addPayroll(@Valid @RequestBody Payroll p){

        Payroll payroll = payrollService.createPayroll(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(payroll);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Payroll> updatePayroll(@Valid @RequestBody Payroll p){

        Payroll payroll = payrollService.updatePayroll(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(payroll);

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




}
