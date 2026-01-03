package com.hr_management_system.department;


import com.hr_management_system.employee.Employee;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartment(@PathVariable Long id){
        Department department = departmentService.findDepartment(id);
        return ResponseEntity.ok(department);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable("id") Long id) {
        try {
            departmentService.deleteDepartment(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments(){


        List<Department> list = departmentService.findAllDepartments();
        return ResponseEntity.ok(list);

    }

    @PostMapping
    public ResponseEntity<Department> createDepartment(@Valid @RequestBody Department d){

        Department department = departmentService.createDepartment(d);

        return ResponseEntity.status(HttpStatus.CREATED).body(department);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable("id")Long id, @Valid @RequestBody Department d){

        if(!id.equals(d.getId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(d);
        }

        Department department = departmentService.updateDepartment(d);
        return ResponseEntity.status(HttpStatus.OK).body(department);

    }




}
