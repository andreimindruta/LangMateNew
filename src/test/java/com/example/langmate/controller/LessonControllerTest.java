package com.example.langmate.controller;
import com.example.langmate.controller.payload.response.GetLessonsResponse;
import com.example.langmate.service.impl.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

public class LessonControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LessonService lessonService;

    @InjectMocks
    private LessonController lessonController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = standaloneSetup(lessonController).build();
    }

    @Test
    public void testGetLessons() throws Exception {
        String languageName = "Spanish";
        GetLessonsResponse mockResponse = new GetLessonsResponse(Collections.emptyList());
        when(lessonService.getLessons(languageName, "test-jwt-token")).thenReturn(mockResponse);

        mockMvc.perform(get("/langmate/lesson/" + languageName)
                .cookie(new jakarta.servlet.http.Cookie("JWT", "test-jwt-token")))
                .andExpect(status().isOk())
                .andExpect(view().name("lesson"));

        verify(lessonService).getLessons(languageName, "test-jwt-token");
    }
}
