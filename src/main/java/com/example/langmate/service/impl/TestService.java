package com.example.langmate.service.impl;

import com.example.langmate.controller.payload.request.GetQuestionRequest;
import com.example.langmate.controller.payload.request.PostAnswerRequest;
import com.example.langmate.controller.payload.request.PostTestResultRequest;
import com.example.langmate.controller.payload.response.GetQuestionResponse;
import com.example.langmate.controller.payload.response.GetQuestionsResponse;
import com.example.langmate.controller.payload.response.GetResultResponse;
import com.example.langmate.domain.entities.Result;
import com.example.langmate.repository.LanguageRepository;
import com.example.langmate.repository.QuestionRepository;
import com.example.langmate.repository.ResultRepository;
import com.example.langmate.validation.LangmateRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestService {

  private final QuestionRepository questionRepository;

  private final LanguageRepository languageRepository;

  private final UserService userService;

  private final ResultRepository resultRepository;

  private final MilestoneService milestoneService;

  SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

  public GetQuestionsResponse findQuestionsForTest(final GetQuestionRequest request)
      throws LangmateRuntimeException {
    val language =
        languageRepository
            .findByName(request.languageName())
            .orElseThrow(() -> new LangmateRuntimeException(404, "Language not found"));
    val questions = questionRepository.findAllByLanguage(language);

    Collections.shuffle(questions);
    val reducedListOfQuestions = questions.subList(0, 5);
    val mappedQuestions =
        reducedListOfQuestions.stream()
            .map(question -> new GetQuestionResponse(question.getQuestion()))
            .toList();
    return new GetQuestionsResponse(mappedQuestions);
  }

    public GetResultResponse saveResultForTest(final PostTestResultRequest request, String jwt)
            throws LangmateRuntimeException {

        val points = request.answers().stream().map(this::mapQuestionAndAnswerToPoint).toList();

        val language = languageRepository
                .findByName(request.languageName())
                .orElseThrow(() -> new LangmateRuntimeException(400, "Language does not exist"));

        val grade = points.stream().mapToDouble(Integer::doubleValue).sum();

        val user = userService.getCurrentUser(jwt)
                .orElseThrow(() -> new LangmateRuntimeException(403, "User not found"));

        val result = Result.builder()
                .user(user)
                .timestamp(new Date())
                .grade(grade)
                .language(language)
                .build();

        val savedResult = resultRepository.save(result);

        milestoneService.checkAndAssignMilestones(user.getId(), language.getId());

        return new GetResultResponse(
                savedResult.getGrade(),
                savedResult.getLanguage().getName(),
                formatter.format(result.getTimestamp()));
    }


    private Integer mapQuestionAndAnswerToPoint(PostAnswerRequest request) {
    val question = questionRepository.findByQuestion(request.question());
    if (question.getAnswer().toLowerCase().equals(request.answer().toLowerCase())) {
      return 1;
    }
    return 0;
  }

  public java.util.List<String> getQuestionsForLanguage(String languageName) throws LangmateRuntimeException {
    log.info("Looking for questions for language: '{}'", languageName);
    
    val language = languageRepository.findByName(languageName)
        .orElseThrow(() -> new LangmateRuntimeException(404, "Language not found"));
    
    log.info("Found language: '{}' with ID: {}", language.getName(), language.getId());
    
    val questions = questionRepository.findAllByLanguage(language);
    log.info("Found {} questions for language: '{}'", questions.size(), languageName);
    
    Collections.shuffle(questions);
    return questions.stream().limit(5).map(q -> q.getQuestion()).toList();
  }

  public String getJwtForUser(String username) throws LangmateRuntimeException {
    val user = userService.getUserByUsername(username)
        .orElseThrow(() -> new LangmateRuntimeException(403, "User not found"));
    return userService.generateJwtForUser(user);
  }
}
