package com.hr_management_system.position;


import com.hr_management_system.employee.Employee;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/positions")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<Position> getPosition(@PathVariable Long id){


        try{
            Position position = positionService.findPosition(id);
            return ResponseEntity.status(HttpStatus.OK).body(position);

        }catch (EntityNotFoundException e){
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((Position) errorResponse);

    }}


    @GetMapping
    public ResponseEntity<List<Position>> getAllEmployees() {

        List<Position> list = positionService.findAllPositions();
        return ResponseEntity.ok(list);

    }




    @PostMapping
    public ResponseEntity<Position> addPosition(@Valid @RequestBody Position p){

        Position position = positionService.createPosition(p);

        return ResponseEntity.status(HttpStatus.CREATED).body(position);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Position> updatePosition(@PathVariable("id")Long id, @Valid @RequestBody Position p){

        if(!p.getId().equals(id)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(p);
        }

        Position position = positionService.updatePosition(p);

        return ResponseEntity.status(HttpStatus.OK).body(position);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable("id")Long id){

        try{

            positionService.deletePosition(id);
            return ResponseEntity.noContent().build();


        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();


        }


    }



}
