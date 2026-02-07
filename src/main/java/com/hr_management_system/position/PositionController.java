package com.hr_management_system.position;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/positions")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PositionResponse> getPosition(@PathVariable Long id) {
        PositionResponse position = positionService.findPosition(id);
        return ResponseEntity.ok(position);
    }

    @GetMapping
    public ResponseEntity<Page<PositionResponse>> getAllPositions(@PageableDefault(size = 20) Pageable pageable) {
        Page<PositionResponse> page = positionService.findAllPositions(pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<PositionResponse> addPosition(@Valid @RequestBody PositionRequest request) {
        PositionResponse position = positionService.createPosition(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(position);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PositionResponse> updatePosition(@PathVariable("id") Long id,
                                                           @Valid @RequestBody PositionRequest request) {
        PositionResponse position = positionService.updatePosition(id, request);
        return ResponseEntity.ok(position);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable("id") Long id) {
        positionService.deletePosition(id);
        return ResponseEntity.noContent().build();
    }
}
