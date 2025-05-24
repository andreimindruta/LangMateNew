package com.example.langmate.controller;

import com.example.langmate.controller.payload.request.MilestoneRequest;
import com.example.langmate.controller.payload.response.GetMilestoneResponse;
import com.example.langmate.domain.entities.Milestone;
import com.example.langmate.domain.entities.MilestoneTargetType;
import com.example.langmate.service.impl.MilestoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MilestoneControllerTest {

    @InjectMocks
    private MilestoneController milestoneController;

    @Mock
    private MilestoneService milestoneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMilestoneById_Success() {
        // arrange
        Long milestoneId = 1L;
        Milestone milestone = new Milestone();
        milestone.setId(milestoneId);
        milestone.setName("Test Milestone");
        milestone.setDescription("Test description");
        milestone.setTargetType(MilestoneTargetType.GRADE);
        milestone.setTargetValue(100);

        when(milestoneService.getMilestoneById(milestoneId)).thenReturn(milestone);

        // act
        ResponseEntity<GetMilestoneResponse> response = milestoneController.getMilestoneById(milestoneId);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(milestoneId, response.getBody().id());
        assertEquals("Test Milestone", response.getBody().name());
        assertEquals("Test description", response.getBody().description());
        assertEquals(100, response.getBody().targetValue());
        verify(milestoneService, times(1)).getMilestoneById(milestoneId);
    }

    @Test
    void testGetMilestoneById_NotFound() {
        // arrange
        Long milestoneId = 1L;

        when(milestoneService.getMilestoneById(milestoneId)).thenThrow(new RuntimeException("Milestone not found"));

        // act
        Exception exception = assertThrows(RuntimeException.class, () -> {
            milestoneController.getMilestoneById(milestoneId);
        });

        // assert
        assertEquals("Milestone not found", exception.getMessage());
        verify(milestoneService, times(1)).getMilestoneById(milestoneId);
    }

    @Test
    void testAddMilestone_Success() {
        // arrange
        MilestoneRequest milestoneRequest = new MilestoneRequest(
                "New Milestone",
                "Description of milestone",
                100,
                "TargetType"
        );

        Milestone milestone = new Milestone();
        milestone.setId(1L);
        milestone.setName("New Milestone");
        milestone.setDescription("Description of milestone");
        milestone.setTargetType(MilestoneTargetType.GRADE);
        milestone.setTargetValue(100);

        when(milestoneService.createMilestone(any(MilestoneRequest.class))).thenReturn(milestone);

        // act
        ResponseEntity<GetMilestoneResponse> response = milestoneController.createMilestone(milestoneRequest);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals("New Milestone", response.getBody().name());
        assertEquals("Description of milestone", response.getBody().description());
        assertEquals(100, response.getBody().targetValue());
        verify(milestoneService, times(1)).createMilestone(any(MilestoneRequest.class));
    }
}
