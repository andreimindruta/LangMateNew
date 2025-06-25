package com.example.langmate.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.langmate.controller.payload.response.GetLessonsResponse;
import com.example.langmate.domain.entities.Language;
import com.example.langmate.domain.entities.Lesson;
import com.example.langmate.domain.entities.User;
import com.example.langmate.repository.LanguageRepository;
import com.example.langmate.repository.LessonRepository;
import com.example.langmate.service.impl.LessonService;
import com.example.langmate.service.impl.UserService;
import com.example.langmate.validation.LangmateRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

public class LessonServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private LessonService lessonService;

    private final String jwt = "test-jwt-token";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getLessons_UserNotFound_ThrowsException() {
        String languageName = "Spanish";
        when(userService.getCurrentUser(jwt)).thenReturn(Optional.empty());

        assertThrows(LangmateRuntimeException.class, () -> {
            lessonService.getLessons(languageName, jwt);
        });
    }

    @Test
    public void getLessons_UserNotEnrolledInAnyLanguages_ThrowsException() {
        String languageName = "Spanish";
        User mockUser = mock(User.class);
        when(userService.getCurrentUser(jwt)).thenReturn(Optional.of(mockUser));
        when(mockUser.getLanguages()).thenReturn(List.of());
        assertThrows(LangmateRuntimeException.class, () -> {
            lessonService.getLessons(languageName, jwt);
        });
    }

    @Test
    public void getLessons_Successful_ReturnsLessons() throws LangmateRuntimeException {
        String languageName = "es";
        User mockUser = mock(User.class);
        Language mockLanguage = Language.builder().name(languageName).id(1L).build();
        Lesson mockLesson = Lesson.builder().id(1L).title("Greetings").text("Hola means Hi").build();

        when(userService.getCurrentUser(jwt)).thenReturn(Optional.of(mockUser));
        when(mockUser.getLanguages()).thenReturn(List.of(mockLanguage));
        when(languageRepository.findByName(languageName)).thenReturn(Optional.of(mockLanguage));
        when(lessonRepository.findAllByLanguage(mockLanguage)).thenReturn(List.of(mockLesson));

        GetLessonsResponse response = lessonService.getLessons(languageName, jwt);
        assertNotNull(response);
        assertFalse(response.lessons().isEmpty());
        assertEquals("Greetings", response.lessons().get(0).title());
        assertEquals("Hola means Hi", response.lessons().get(0).text());
    }
}
