package com.example.langmate.service.impl;

import com.example.langmate.controller.payload.response.GetLanguageResponse;
import com.example.langmate.controller.payload.response.GetLanguagesResponse;
import com.example.langmate.domain.entities.Language;
import com.example.langmate.repository.LanguageRepository;
import com.example.langmate.repository.UserRepository;
import com.example.langmate.service.impl.UserService;
import com.example.langmate.validation.LangmateRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LanguageService {

  private final LanguageRepository languageRepository;
  private final UserService userService;
  private final UserRepository userRepository;

  public GetLanguagesResponse getUserLanguages(String jwt) throws LangmateRuntimeException {
    val currentUser =
        userService
            .getCurrentUser(jwt)
            .orElseThrow(
                () -> new LangmateRuntimeException(403, "Please login to access this feature"));
    val userLanguages =
        currentUser.getLanguages().stream()
            .map(language -> new GetLanguageResponse(language.getName()))
            .toList();
    return new GetLanguagesResponse(userLanguages);
  }

  public GetLanguagesResponse getLanguages() {
    val languages =
        languageRepository.findAll().stream()
            .map(l -> new GetLanguageResponse(l.getName()))
            .toList();
    return new GetLanguagesResponse(languages);
  }

  public Page<GetLanguageResponse> getLanguagesPaginated(Pageable pageable) {
    log.info("Getting languages with pagination - page: {}, size: {}, sort: {}", 
             pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
    
    Page<Language> languagesPage = languageRepository.findAll(pageable);
    
    log.info("Found {} languages on page {} of {}", 
             languagesPage.getContent().size(), 
             languagesPage.getNumber(), 
             languagesPage.getTotalPages());
    
    // Log each language name to debug
    languagesPage.getContent().forEach(language -> {
      log.info("Language ID: {}, Name: '{}'", language.getId(), language.getName());
    });
    
    Page<GetLanguageResponse> responsePage = languagesPage
        .map(l -> new GetLanguageResponse(l.getName()));
    
    log.info("Returning {} language responses", responsePage.getContent().size());
    
    return responsePage;
  }

  public GetLanguagesResponse addLanguageToUser(final String languageName, String jwt)
      throws LangmateRuntimeException {
    val currentUser =
        userService
            .getCurrentUser(jwt)
            .orElseThrow(
                () -> new LangmateRuntimeException(403, "Please login to access this api"));
    val language =
        languageRepository
            .findByName(languageName)
            .orElseThrow(() -> new LangmateRuntimeException(400, "Language does not exist"));
    val userLanguages = currentUser.getLanguages();
    userLanguages.add(language);
    currentUser.setLanguages(userLanguages);
    val updatedUser = userRepository.save(currentUser);
    val updatedUserLanguages =
        updatedUser.getLanguages().stream().map(l -> new GetLanguageResponse(l.getName())).toList();
    return new GetLanguagesResponse(updatedUserLanguages);
  }
}
