package com.hr_management_system.payroll;

public enum PayrollStatusEnum {

    PENDING("Pending"),
    ONGOING("Ongoing"),
    COMPLETE("Complete"),
    FAILED("Failed");

    private final String statusName;

    PayrollStatusEnum(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}
