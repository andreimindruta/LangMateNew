package com.example.langmate.integration;//package com.example.langmate.integration;
//
//import com.example.langmate.controller.payload.response.GetLanguagesResponse;
//import com.example.langmate.domain.entities.Language;
//import com.example.langmate.domain.entities.Lesson;
//import com.example.langmate.domain.entities.Question;
//import com.example.langmate.repository.LanguageRepository;
//import com.example.langmate.repository.LessonRepository;
//import com.example.langmate.repository.QuestionRepository;
//import com.example.langmate.repository.UserRepository;
//import com.example.langmate.service.impl.LanguageService;
//import com.example.langmate.service.impl.UserService;
//import com.example.langmate.utils.LangmateTestUtils;
//import com.example.langmate.utils.JwtUtils;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//@SpringBootTest()
//@ExtendWith(SpringExtension.class)
//public class LanguageServiceIntegrationTest {
//    @Mock
//    LanguageRepository languageRepository;
//
//    @Autowired
//    LessonRepository lessonRepository;
//
//    @Autowired
//    QuestionRepository questionRepository;
//
//
//    @Autowired
//    LanguageService languageService;
//
//    @Mock
//    JwtUtils jwtUtils;
//
//    @Mock
//    UserRepository userRepository;
//
//    @InjectMocks
//    UserService userService;
//
//
//
//    @BeforeEach
//    public void init() {
//        Language language = new Language();
//        language.setId(1L);
//        language.setName("franceza");
//
//        Lesson lesson = Lesson.builder().build();
//        lesson.setId(1L);
//        lesson.setText("Lectia numarul 1");
//        lesson.setTitle("Conjugarea verbelor");
//        lessonRepository.save(lesson);
//
//        List<Lesson> lessons = new ArrayList<>();
//        lessons.add(lesson);
//        language.setLessons(lessons);
//
//        Question question = new Question();
//        question.setId(1L);
//        question.setQuestion("Cum se traduce cuvantul cartof?");
//        question.setAnswer("pomme de terre");
//        questionRepository.save(question);
//
//        List<Question> questions = new ArrayList<>();
//        questions.add(question);
//        language.setQuestions(questions);
//
//        languageRepository.save(language);
//
//        Mockito.when(userService.getCurrentUser()).thenReturn(LangmateTestUtils.mockNewUser());
//        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.any())).thenReturn("andrei");
//        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(LangmateTestUtils.mockNewUser());
//    }
//
//    @Test
//    void shouldGetLanguages() {
//        GetLanguagesResponse languageResponse = languageService.getLanguages();
//        Assertions.assertEquals(languageResponse.languages().size(), 1);
//        Assertions.assertEquals(languageResponse.languages().get(0).name(), "franceza");
//    }
//
////    @Test
////    void shouldEnrollNewLanguage() throws LangmateRuntimeException {
////        GetLanguagesResponse languagesResponse = languageService.addLanguageToUser("italiana");
////    }
//}
