package com.example.langmate.service.impl;

import com.example.langmate.controller.payload.response.GetLessonResponse;
import com.example.langmate.controller.payload.response.GetLessonsResponse;
import com.example.langmate.repository.LanguageRepository;
import com.example.langmate.repository.LessonRepository;
import com.example.langmate.validation.LangmateRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonService {

  private final UserService userService;
  private final LessonRepository lessonRepository;
  private final LanguageRepository languageRepository;

  public GetLessonsResponse getLessons(final String languageName, String jwt)
      throws LangmateRuntimeException {

    val currentUser =
        userService
            .getCurrentUser(jwt)
            .orElseThrow(() -> new LangmateRuntimeException(403, "User not found"));

    val userLanguages = currentUser.getLanguages();
    if (userLanguages.isEmpty()) {
      throw new LangmateRuntimeException(404, "User not enrolled in any languages");
    }
    val language =
        languageRepository
            .findByName(languageName)
            .orElseThrow(() -> new LangmateRuntimeException(400, "Language does not exist"));
    if (!userLanguages.contains(language)) {
      throw new LangmateRuntimeException(400, "User not enrolled in this language");
    }
    val lessons =
        lessonRepository.findAllByLanguage(language).stream()
            .map(lesson -> new GetLessonResponse(lesson.getTitle(), lesson.getText()))
            .toList();
    return new GetLessonsResponse(lessons);
  }
}
