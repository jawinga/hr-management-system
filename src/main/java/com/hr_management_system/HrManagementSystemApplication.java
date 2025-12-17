package com.hr_management_system;

import com.hr_management_system.employee.Employee;
import com.hr_management_system.position.Position;
import jakarta.validation.Valid;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class HrManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HrManagementSystemApplication.class, args);
		Employee employee1 = new Employee();
		Position position1 = new Position();

		position1.setMinSalary(new BigDecimal("10000"));
		position1.setMaxSalary(new BigDecimal("20000"));

		employee1.setPosition(position1);

		// Test valid salary
		employee1.setSalary(new BigDecimal("15000"));
		System.out.println("Valid salary (15000): " + employee1.isValidSalary());

		// Test invalid salary
		employee1.setSalary(new BigDecimal("25000"));
		System.out.println("Invalid salary (25000): " + employee1.isValidSalary());

	}

}
