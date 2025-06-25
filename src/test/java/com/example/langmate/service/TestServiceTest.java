package com.example.langmate.service;

import com.example.langmate.controller.payload.request.GetQuestionRequest;
import com.example.langmate.controller.payload.request.PostAnswerRequest;
import com.example.langmate.controller.payload.request.PostTestResultRequest;
import com.example.langmate.controller.payload.response.GetResultResponse;
import com.example.langmate.domain.entities.Language;
import com.example.langmate.domain.entities.Question;
import com.example.langmate.domain.entities.Result;
import com.example.langmate.domain.entities.User;
import com.example.langmate.repository.LanguageRepository;
import com.example.langmate.repository.QuestionRepository;
import com.example.langmate.repository.ResultRepository;
import com.example.langmate.service.impl.TestService;
import com.example.langmate.service.impl.UserService;
import com.example.langmate.service.impl.MilestoneService;
import com.example.langmate.validation.LangmateRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class TestServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private UserService userService;

    @Mock
    private ResultRepository resultRepository;

    @Mock
    private MilestoneService milestoneService;

    @InjectMocks
    private TestService testService;

    private final String jwt = "test-jwt-token";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // stub out milestone checks so they don't throw
        doNothing().when(milestoneService).checkAndAssignMilestones(anyLong(), anyLong());
    }

    @Test
    public void findQuestionsForTest_LanguageNotFound_ThrowsException() {
        GetQuestionRequest request = new GetQuestionRequest("Spanish");
        when(languageRepository.findByName("Spanish")).thenReturn(Optional.empty());

        assertThrows(LangmateRuntimeException.class, () -> {
            testService.findQuestionsForTest(request);
        });
    }

    @Test
    public void saveResultForTest_LanguageDoesNotExist_ThrowsException() {
        PostTestResultRequest request = new PostTestResultRequest(Collections.emptyList(), "Spanish");
        when(languageRepository.findByName("Spanish")).thenReturn(Optional.empty());

        assertThrows(LangmateRuntimeException.class, () -> {
            testService.saveResultForTest(request, jwt);
        });
    }

    @Test
    public void saveResultForTest_Successful_ReturnsResultResponse() throws LangmateRuntimeException {
        // prepare inputs
        PostAnswerRequest pan1 = new PostAnswerRequest("Translate to Spanish: Good morning.", "Buenos dias");
        PostAnswerRequest pan2 = new PostAnswerRequest("Translate to Spanish: Good evening.", "Buenas tardes");
        PostTestResultRequest request = new PostTestResultRequest(List.of(pan1, pan2), "Spanish");

        // prepare domain objects
        Language language = new Language(1L, "Spanish", Collections.emptyList(), Collections.emptyList());
        User user = new User();
        Question question = Question.builder()
                .question("Translate to Spanish: Good evening.")
                .answer("Buenas tardes")
                .build();
        Result result = Result.builder()
                .grade(10D)
                .language(language)
                .build();

        // stubbing repositories & services
        when(languageRepository.findByName("Spanish")).thenReturn(Optional.of(language));
        when(userService.getCurrentUser(jwt)).thenReturn(Optional.of(user));
        when(questionRepository.findByQuestion(any())).thenReturn(question);
        when(resultRepository.save(any(Result.class))).thenReturn(result);

        // invoke
        GetResultResponse resp = testService.saveResultForTest(request, jwt);

        // verify
        assertNotNull(resp);
        assertEquals(10D, resp.grade());
    }
}
