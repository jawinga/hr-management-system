package com.hr_management_system.department;

import lombok.Getter;

@Getter
public enum DepartmentEnum {
    HR("Handles recruitment, employee relations, and payroll."),
    FINANCE("Manages budgeting, financial planning, and audits."),
    ENGINEERING("Responsible for software development and technical infrastructure."),
    MARKETING("Focuses on branding, advertising, and market research."),
    SALES("Responsible for generating revenue and client acquisition."),
    CUSTOMER_SUPPORT("Assists clients with technical issues and inquiries."),
    LEGAL("Handles contracts, compliance, and regulatory matters."),
    OPERATIONS("Manages day-to-day business processes and efficiency.");

    private final String description;

    DepartmentEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
