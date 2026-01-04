package com.hr_management_system.config;

import com.hr_management_system.department.Department;
import com.hr_management_system.department.DepartmentEnum;
import com.hr_management_system.department.DepartmentRepository;
import com.hr_management_system.employee.Employee;
import com.hr_management_system.employee.EmployeeRepository;
import com.hr_management_system.employee.EmployeeRole;
import com.hr_management_system.position.Position;
import com.hr_management_system.position.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {


    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;


    public DataSeeder(DepartmentRepository departmentRepository, PositionRepository positionRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("CommandLineRunner started");

        departmentRepository.deleteAll();
        positionRepository.deleteAll();
        employeeRepository.deleteAll();

        //positions
        Position softwareEngineer = new Position();
        softwareEngineer.setPositionTitle("Software Engineer");
        softwareEngineer.setMinSalary(new BigDecimal("50000"));
        softwareEngineer.setMaxSalary(new BigDecimal("80000"));
        positionRepository.save(softwareEngineer);

        Position seniorSoftwareEngineer = new Position();
        seniorSoftwareEngineer.setPositionTitle("Senior Software Engineer");
        seniorSoftwareEngineer.setMinSalary(new BigDecimal("80000"));
        seniorSoftwareEngineer.setMaxSalary(new BigDecimal("120000"));
        positionRepository.save(seniorSoftwareEngineer);

        Position productManager = new Position();
        productManager.setPositionTitle("Product Manager");
        productManager.setMinSalary(new BigDecimal("70000"));
        productManager.setMaxSalary(new BigDecimal("110000"));
        positionRepository.save(productManager);

        //departments
        Department engineering = new Department();
        engineering.setDepartmentName(DepartmentEnum.ENGINEERING);
        engineering.setActive(true);
        engineering.setContactDept("engineering@company.com");
        departmentRepository.save(engineering);

        Department hr = new Department();
        hr.setDepartmentName(DepartmentEnum.HR);
        hr.setActive(true);
        hr.setContactDept("hr@company.com");
        departmentRepository.save(hr);

        Department sales = new Department();
        sales.setDepartmentName(DepartmentEnum.SALES);
        sales.setActive(true);
        sales.setContactDept("sales@company.com");
        departmentRepository.save(sales);

        //employees

        Employee john = new Employee();
        john.setFirstName("John");
        john.setLastName("Doe");
        john.setCompanyEmail("john.doe@company.com");
        john.setEmail("john.personal@email.com");
        john.setPhoneNumber("+34 612 345 678");
        john.setDateOfBirth(LocalDate.of(1990, 5, 15));
        john.setRole(EmployeeRole.EMPLOYEE);
        john.setSalary(new BigDecimal("65000"));
        john.setSocialSecurity(123456789);
        john.setBankDetails(987654321);
        john.setPosition(softwareEngineer);
        john.setDepartments(List.of(engineering));
        employeeRepository.save(john);

        Employee jane = new Employee();
        jane.setFirstName("Jane");
        jane.setLastName("Smith");
        jane.setCompanyEmail("jane.smith@company.com");
        jane.setEmail("jane.personal@email.com");
        jane.setPhoneNumber("+34 623 456 789");
        jane.setDateOfBirth(LocalDate.of(1985, 8, 22));
        jane.setRole(EmployeeRole.TEAM_LEADER);
        jane.setSalary(new BigDecimal("95000"));
        jane.setSocialSecurity(234567890);
        jane.setBankDetails(876543210);
        jane.setPosition(seniorSoftwareEngineer);
        jane.setDepartments(List.of(engineering));
        employeeRepository.save(jane);

        Employee mike = new Employee();
        mike.setFirstName("Mike");
        mike.setLastName("Johnson");
        mike.setCompanyEmail("mike.johnson@company.com");
        mike.setEmail("mike.personal@email.com");
        mike.setPhoneNumber("+34 634 567 890");
        mike.setDateOfBirth(LocalDate.of(1988, 3, 10));
        mike.setRole(EmployeeRole.MANAGER);
        mike.setSalary(new BigDecimal("85000"));
        mike.setSocialSecurity(345678901);
        mike.setBankDetails(765432109);
        mike.setPosition(productManager);
        mike.setDepartments(List.of(hr, sales));
        employeeRepository.save(mike);

        System.out.println("Seeding ended.");
    }
}


