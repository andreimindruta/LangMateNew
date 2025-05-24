package com.example.langmate.controller;

import com.example.langmate.controller.payload.request.GetQuestionRequest;
import com.example.langmate.controller.payload.request.PostTestResultRequest;
import com.example.langmate.controller.payload.response.GetQuestionsResponse;
import com.example.langmate.controller.payload.request.PostAnswerRequest;
import com.example.langmate.controller.payload.response.GetResultResponse;
import com.example.langmate.service.impl.TestService;
import com.example.langmate.validation.LangmateRuntimeException;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/langmate/tests")
@RequiredArgsConstructor
@Slf4j
public class TestController {

  private final TestService testService;

  @PostMapping(path = "/questions")
  public ResponseEntity<GetQuestionsResponse> findQuestionsForTest(@RequestBody @NonNull final
      GetQuestionRequest request) throws LangmateRuntimeException {
    return ResponseEntity.ok(testService.findQuestionsForTest(request));
  }

  @PostMapping(path = "/answers")
  public ResponseEntity<GetResultResponse> saveResultForTest(@RequestBody @NonNull final
  PostTestResultRequest request) throws LangmateRuntimeException {
    return ResponseEntity.ok(testService.saveResultForTest(request));
  }

}
