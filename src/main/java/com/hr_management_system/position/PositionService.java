package com.hr_management_system.position;

import com.hr_management_system.department.Department;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {

    @Autowired
    private final PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    Position createPosition(Position position){

        if(positionRepository.existsByPositionTitle(position.getPositionTitle())){
            throw new RuntimeException("Position already exists!");
        }
        System.out.println(position);
        return positionRepository.save(position);

    }

    Position findPosition(Long id){
        if(id == null) throw new IllegalArgumentException("Id not provided");

        return positionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Position not found"));
    }

    List<Position> findAllPositions(){

        return positionRepository.findAll();

    }


    void deletePosition(Long id){

        if(!positionRepository.existsById(id)){
            throw new RuntimeException("Department could not be found!");

        }

        positionRepository.deleteById(id);


    }


    Position updatePosition(Position position){

        if(!positionRepository.existsById(position.getId())){
            throw new EntityNotFoundException("Department with ID " + position.getId() + " not found");

        }

        return positionRepository.save(position);

    }

}
