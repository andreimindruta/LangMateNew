package com.example.langmate.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.example.langmate.controller.payload.response.GetLanguagesResponse;
import com.example.langmate.service.impl.LanguageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

public class LanguagesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LanguageService languageService;

    @InjectMocks
    private LanguagesController languagesController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = standaloneSetup(languagesController).build();
    }

    @Test
    public void testGetUserLanguages() throws Exception {
        GetLanguagesResponse mockResponse = new GetLanguagesResponse(Collections.emptyList());
        when(languageService.getUserLanguages()).thenReturn(mockResponse);

        mockMvc.perform(get("/langmate/language"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(languageService).getUserLanguages();
    }

    @Test
    public void testGetLanguages() throws Exception {
        GetLanguagesResponse mockResponse = new GetLanguagesResponse(Collections.emptyList());
        when(languageService.getLanguages()).thenReturn(mockResponse);

        mockMvc.perform(get("/langmate/language/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(languageService).getLanguages();
    }

    @Test
    public void testAddLanguage() throws Exception {
        String languageName = "Spanish";
        GetLanguagesResponse mockResponse = new GetLanguagesResponse(Collections.emptyList());
        when(languageService.addLanguageToUser(languageName)).thenReturn(mockResponse);

        mockMvc.perform(post("/langmate/language/enroll/" + languageName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(languageService).addLanguageToUser(languageName);
    }
}