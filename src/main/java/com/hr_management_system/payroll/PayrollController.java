package com.hr_management_system.payroll;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payroll")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @PostMapping
    public ResponseEntity<PayrollResponse> addPayroll(@Valid @RequestBody PayrollRequest request) {
        PayrollResponse payroll = payrollService.createPayroll(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payroll);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PayrollResponse> updatePayroll(@PathVariable("id") Long id,
                                                         @Valid @RequestBody PayrollRequest request) {
        PayrollResponse payroll = payrollService.updatePayroll(id, request);
        return ResponseEntity.ok(payroll);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayroll(@PathVariable("id") Long id) {
        payrollService.deletePayroll(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayrollResponse> getPayroll(@PathVariable("id") Long id) {
        PayrollResponse payroll = payrollService.findPayroll(id);
        return ResponseEntity.ok(payroll);
    }

    @GetMapping
    public ResponseEntity<Page<PayrollResponse>> getAllPayrolls(@PageableDefault(size = 20) Pageable pageable) {
        Page<PayrollResponse> page = payrollService.findAllPayrolls(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PayrollResponse>> getPayrollsByEmployee(@PathVariable("employeeId") Long employeeId) {
        List<PayrollResponse> payrolls = payrollService.findByEmployeeId(employeeId);
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PayrollResponse>> getPayrollsByStatus(@PathVariable("status") PayrollStatusEnum status) {
        List<PayrollResponse> payrolls = payrollService.findByPaymentStatus(status);
        return ResponseEntity.ok(payrolls);
    }
}
