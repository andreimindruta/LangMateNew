package com.example.langmate.integration;

import com.example.langmate.controller.payload.response.GetLanguagesResponse;
import com.example.langmate.domain.entities.Language;
import com.example.langmate.domain.entities.Lesson;
import com.example.langmate.domain.entities.Question;
import com.example.langmate.repository.LanguageRepository;
import com.example.langmate.repository.LessonRepository;
import com.example.langmate.repository.QuestionRepository;
import com.example.langmate.repository.UserRepository;
import com.example.langmate.service.impl.LanguageService;
import com.example.langmate.service.impl.UserService;
import com.example.langmate.utils.LangmateTestUtils;
import com.example.langmate.utils.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LanguageServiceIntegrationTest {

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private LanguageService languageService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void init() {
        // Configure SecurityContext for userService.getCurrentUser()
        String fakeJwt = "fake-jwt-token";
        Authentication auth = org.mockito.Mockito.mock(Authentication.class);
        org.mockito.Mockito.when(auth.getCredentials()).thenReturn(fakeJwt);
        SecurityContext sc = org.mockito.Mockito.mock(SecurityContext.class);
        org.mockito.Mockito.when(sc.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(sc);

        // Mock JWT and user lookup
        org.mockito.Mockito.when(jwtUtils.getUserNameFromJwtToken(fakeJwt)).thenReturn("andrei");
        org.mockito.Mockito.when(userRepository.findByUsername("andrei")).thenReturn(LangmateTestUtils.mockNewUser());

        // Clear and seed test data
        languageRepository.deleteAll();
        lessonRepository.deleteAll();
        questionRepository.deleteAll();

        Language language = new Language();
        language.setName("franceza");
        language = languageRepository.save(language);

        Lesson lesson = Lesson.builder()
                .text("Lectia numarul 1")
                .title("Conjugarea verbelor")
                .build();
        lesson.setLanguage(language);
        lessonRepository.save(lesson);

        Question question = new Question();
        question.setQuestion("Cum se traduce cuvantul cartof?");
        question.setAnswer("pomme de terre");
        question.setLanguage(language);
        questionRepository.save(question);
    }

    @Test
    void shouldGetLanguages() {
        GetLanguagesResponse response = languageService.getLanguages();
        List<?> langs = response.languages();
        Assertions.assertEquals(1, langs.size());
        Assertions.assertEquals("franceza", response.languages().get(0).name());
    }
}
