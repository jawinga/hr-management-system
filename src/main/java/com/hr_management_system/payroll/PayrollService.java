package com.hr_management_system.payroll;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PayrollService {

    private final PayrollRepository payrollRepository;

    Payroll createPayroll(Payroll p){

        return p;
    }

    Payroll updatePayroll(Payroll p){
        return  p;
    }

    void deletePayroll(Long id){


    }


}
