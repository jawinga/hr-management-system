package com.hr_management_system.payroll;

public enum PayrollMethodEnum {

    DEPOSIT("Bank Deposit"),
    CHECK("Check"),
    CASH("Cash"),
    PAYPAL("PayPal"),
    WIRE("Wire Transfer"),
    OTHER("Other");


    private final String methodName;

    PayrollMethodEnum(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }
}
