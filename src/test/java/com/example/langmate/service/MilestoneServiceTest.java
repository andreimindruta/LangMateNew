package com.example.langmate.service;

import com.example.langmate.domain.entities.Milestone;
import com.example.langmate.repository.MilestoneRepository;
import com.example.langmate.service.impl.MilestoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MilestoneServiceTest {

    @InjectMocks
    private MilestoneService milestoneService;

    @Mock
    private MilestoneRepository milestoneRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMilestoneById_Success() {
        //arrange
        Long milestoneId = 1L;
        Milestone milestone = new Milestone();
        milestone.setId(milestoneId);
        milestone.setName("Test Milestone");

        when (milestoneRepository.findById(milestoneId)).thenReturn(Optional.of(milestone));

        // act
        Milestone result = milestoneService.getMilestoneById(milestoneId);

        // assert
        assertNotNull(result);
        assertEquals(milestoneId, result.getId());
        assertEquals("Test Milestone", result.getName());
        verify(milestoneRepository, times(1)).findById(milestoneId);
    }

    @Test
    void testGetMilestoneById_NotFound() {
        //arrange
        Long milestoneId = 1L;

        when(milestoneRepository.findById(milestoneId)).thenReturn(Optional.empty());

        //act
        Exception exception = assertThrows(RuntimeException.class, () -> milestoneService.getMilestoneById(milestoneId));

        //assert
        assertEquals("Milestone not found with id: 1", exception.getMessage());
        verify(milestoneRepository, times(1)).findById(milestoneId);
    }

}
