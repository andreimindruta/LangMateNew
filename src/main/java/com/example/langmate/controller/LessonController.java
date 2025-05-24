package com.example.langmate.controller;


import com.example.langmate.controller.payload.response.GetLessonsResponse;
import com.example.langmate.service.impl.LessonService;
import com.example.langmate.validation.LangmateRuntimeException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/langmate/lesson")
@Slf4j
@RequiredArgsConstructor
public class LessonController {

  private final LessonService lessonService;

  @GetMapping(path = "/{languageName}")
  public ResponseEntity<GetLessonsResponse> getLessons(
      @PathVariable @NonNull String languageName) throws LangmateRuntimeException {
      return ResponseEntity.ok(lessonService.getLessons(languageName));
  }

}
