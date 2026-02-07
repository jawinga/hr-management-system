package com.hr_management_system.position;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PositionController.class)
class PositionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PositionService positionService;

    private ObjectMapper objectMapper;
    private PositionRequest positionRequest;
    private PositionResponse positionResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        positionRequest = PositionRequest.builder()
                .positionTitle("Software Engineer")
                .minSalary(new BigDecimal("50000"))
                .maxSalary(new BigDecimal("80000"))
                .build();

        positionResponse = PositionResponse.builder()
                .id(1L)
                .positionTitle("Software Engineer")
                .minSalary(new BigDecimal("50000"))
                .maxSalary(new BigDecimal("80000"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @WithMockUser
    void createPosition_Returns201() throws Exception {
        when(positionService.createPosition(any(PositionRequest.class))).thenReturn(positionResponse);

        mockMvc.perform(post("/api/v1/positions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(positionRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.positionTitle").value("Software Engineer"));
    }

    @Test
    @WithMockUser
    void createPosition_Returns400_ValidationError() throws Exception {
        PositionRequest invalid = PositionRequest.builder().build();

        mockMvc.perform(post("/api/v1/positions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void getPosition_Returns200() throws Exception {
        when(positionService.findPosition(1L)).thenReturn(positionResponse);

        mockMvc.perform(get("/api/v1/positions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.positionTitle").value("Software Engineer"));
    }

    @Test
    @WithMockUser
    void getPosition_Returns404() throws Exception {
        when(positionService.findPosition(99L)).thenThrow(new EntityNotFoundException("Position not found"));

        mockMvc.perform(get("/api/v1/positions/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void getAllPositions_ReturnsPaginated() throws Exception {
        Page<PositionResponse> page = new PageImpl<>(List.of(positionResponse));
        when(positionService.findAllPositions(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/positions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    @WithMockUser
    void updatePosition_Returns200() throws Exception {
        when(positionService.updatePosition(eq(1L), any(PositionRequest.class))).thenReturn(positionResponse);

        mockMvc.perform(put("/api/v1/positions/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(positionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void deletePosition_Returns204() throws Exception {
        doNothing().when(positionService).deletePosition(1L);

        mockMvc.perform(delete("/api/v1/positions/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getPosition_Returns401_Unauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/positions/1"))
                .andExpect(status().isUnauthorized());
    }
}
