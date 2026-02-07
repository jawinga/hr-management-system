package com.hr_management_system.position;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PositionServiceTest {

    @Mock
    private PositionRepository positionRepository;

    @InjectMocks
    private PositionService positionService;

    private PositionRequest positionRequest;
    private Position position;

    @BeforeEach
    void setUp() {
        positionRequest = PositionRequest.builder()
                .positionTitle("Software Engineer")
                .minSalary(new BigDecimal("50000"))
                .maxSalary(new BigDecimal("80000"))
                .build();

        position = new Position();
        position.setId(1L);
        position.setPositionTitle("Software Engineer");
        position.setMinSalary(new BigDecimal("50000"));
        position.setMaxSalary(new BigDecimal("80000"));
        position.setCreatedAt(LocalDateTime.now());
        position.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createPosition_Success() {
        when(positionRepository.existsByPositionTitle("Software Engineer")).thenReturn(false);
        when(positionRepository.save(any(Position.class))).thenAnswer(invocation -> {
            Position p = invocation.getArgument(0);
            p.setId(1L);
            p.setCreatedAt(LocalDateTime.now());
            p.setUpdatedAt(LocalDateTime.now());
            return p;
        });

        PositionResponse response = positionService.createPosition(positionRequest);

        assertNotNull(response);
        assertEquals("Software Engineer", response.getPositionTitle());
        verify(positionRepository).save(any(Position.class));
    }

    @Test
    void createPosition_AlreadyExists_ThrowsException() {
        when(positionRepository.existsByPositionTitle("Software Engineer")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> positionService.createPosition(positionRequest));
    }

    @Test
    void findPosition_Success() {
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));

        PositionResponse response = positionService.findPosition(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Software Engineer", response.getPositionTitle());
    }

    @Test
    void findPosition_NotFound() {
        when(positionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> positionService.findPosition(1L));
    }

    @Test
    void findAllPositions_ReturnsPaginated() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Position> page = new PageImpl<>(List.of(position));
        when(positionRepository.findAll(pageable)).thenReturn(page);

        Page<PositionResponse> result = positionService.findAllPositions(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void deletePosition_Success() {
        when(positionRepository.existsById(1L)).thenReturn(true);

        positionService.deletePosition(1L);

        verify(positionRepository).deleteById(1L);
    }

    @Test
    void deletePosition_NotFound() {
        when(positionRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> positionService.deletePosition(1L));
    }

    @Test
    void updatePosition_Success() {
        when(positionRepository.existsById(1L)).thenReturn(true);
        when(positionRepository.save(any(Position.class))).thenAnswer(invocation -> {
            Position p = invocation.getArgument(0);
            p.setCreatedAt(LocalDateTime.now());
            p.setUpdatedAt(LocalDateTime.now());
            return p;
        });

        PositionResponse response = positionService.updatePosition(1L, positionRequest);

        assertNotNull(response);
        verify(positionRepository).save(any(Position.class));
    }

    @Test
    void updatePosition_NotFound() {
        when(positionRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> positionService.updatePosition(1L, positionRequest));
    }
}
