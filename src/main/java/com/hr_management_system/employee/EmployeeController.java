package com.hr_management_system.employee;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody Employee e){

        Employee employee = employeeService.createEmployee(e);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id")Long id, @Valid @RequestBody Employee e){

        if(!id.equals(e.getId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e);
        }

        Employee employee = employeeService.updateEmployee(e);
        return ResponseEntity.status(HttpStatus.OK).body(employee);

    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable("id") Long id){

        try{

            Employee employee = employeeService.findEmployee(id);
            return ResponseEntity.status(HttpStatus.OK).body(employee);


        }catch (EntityNotFoundException e){
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        }

    }

    @GetMapping
    public ResponseEntity<?> getAllEmployees() {

            List<Employee> list = employeeService.findAllEmployees();
            return ResponseEntity.ok(list);

    }



}
