package com.example.langmate.controller;

import com.example.langmate.controller.payload.response.GetLanguagesResponse;
import com.example.langmate.service.impl.LanguageService;
import com.example.langmate.validation.LangmateRuntimeException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/langmate/language")
@Slf4j
@RequiredArgsConstructor
public class LanguagesController {

  private final LanguageService languageService;

  @GetMapping()
  public ResponseEntity<GetLanguagesResponse> getUserLanguages() throws LangmateRuntimeException {
    return ResponseEntity.ok(languageService.getUserLanguages());
  }

  @GetMapping(path = "/all")
  public ResponseEntity<GetLanguagesResponse> getLanguages() throws LangmateRuntimeException {
    return ResponseEntity.ok(languageService.getLanguages());
  }

  @PostMapping(path = "/enroll/{languageName}")
  public ResponseEntity<GetLanguagesResponse> addLanguage(@PathVariable  @NonNull String languageName) throws LangmateRuntimeException {
    return ResponseEntity.ok(languageService.addLanguageToUser(languageName));
  }

}
