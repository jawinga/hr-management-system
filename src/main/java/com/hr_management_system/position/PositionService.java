package com.hr_management_system.position;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PositionService {

    private static final Logger log = LoggerFactory.getLogger(PositionService.class);

    private final PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    PositionResponse createPosition(PositionRequest request) {
        if (positionRepository.existsByPositionTitle(request.getPositionTitle())) {
            throw new RuntimeException("Position already exists!");
        }

        Position position = mapRequestToEntity(request);
        Position saved = positionRepository.save(position);
        log.info("Created position with ID {}", saved.getId());
        return PositionResponse.fromEntity(saved);
    }

    PositionResponse findPosition(Long id) {
        if (id == null) throw new IllegalArgumentException("Id not provided");

        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Position not found"));
        return PositionResponse.fromEntity(position);
    }

    Page<PositionResponse> findAllPositions(Pageable pageable) {
        return positionRepository.findAll(pageable).map(PositionResponse::fromEntity);
    }

    void deletePosition(Long id) {
        if (!positionRepository.existsById(id)) {
            throw new EntityNotFoundException("Position could not be found!");
        }
        positionRepository.deleteById(id);
        log.info("Deleted position with ID {}", id);
    }

    PositionResponse updatePosition(Long id, PositionRequest request) {
        if (!positionRepository.existsById(id)) {
            throw new EntityNotFoundException("Position with ID " + id + " not found");
        }

        Position position = mapRequestToEntity(request);
        position.setId(id);

        Position saved = positionRepository.save(position);
        log.info("Updated position with ID {}", saved.getId());
        return PositionResponse.fromEntity(saved);
    }

    private Position mapRequestToEntity(PositionRequest request) {
        Position position = new Position();
        position.setPositionTitle(request.getPositionTitle());
        position.setMinSalary(request.getMinSalary());
        position.setMaxSalary(request.getMaxSalary());
        return position;
    }
}
