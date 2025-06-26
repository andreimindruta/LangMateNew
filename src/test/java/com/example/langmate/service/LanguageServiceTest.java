package com.example.langmate.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.langmate.controller.payload.response.GetLanguageResponse;
import com.example.langmate.domain.entities.Language;
import com.example.langmate.domain.entities.Lesson;
import com.example.langmate.domain.entities.Question;
import com.example.langmate.domain.entities.Result;
import com.example.langmate.domain.entities.User;
import com.example.langmate.repository.LanguageRepository;
import com.example.langmate.service.impl.LanguageService;
import com.example.langmate.service.impl.UserService;
import com.example.langmate.validation.LangmateRuntimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@ExtendWith(MockitoExtension.class)
public class LanguageServiceTest {


  public static final String MOCK_LANGUAGE = "MOCK_LANGUAGE";
  @Mock
  private LanguageRepository languageRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private LanguageService languageService;


  @Test
  void getLanguagesTest() {
    val languagesList = List.of(getOneLanguage().get());
    val expectedResponse = languagesList
        .stream().map(l -> new GetLanguageResponse(l.getName())).collect(
        Collectors.toList());
    when(languageRepository.findAll()).thenReturn(languagesList);
    val response = languageService.getLanguages();
    val actualResponse = response.languages();
    Assertions.assertIterableEquals(expectedResponse,actualResponse);
  }

  @Test
  void getLanguagesPaginatedTest() {
    val languagesList = List.of(getOneLanguage().get());
    Pageable pageable = PageRequest.of(0, 5);
    Page<Language> mockPage = new PageImpl<>(languagesList, pageable, 1);
    
    when(languageRepository.findAll(pageable)).thenReturn(mockPage);
    
    Page<GetLanguageResponse> response = languageService.getLanguagesPaginated(pageable);
    
    Assertions.assertEquals(1, response.getTotalElements());
    Assertions.assertEquals(1, response.getContent().size());
    Assertions.assertEquals(MOCK_LANGUAGE, response.getContent().get(0).name());
  }



  private Optional<Language> getOneLanguage(){
    val language = new Language();
    language.setName(MOCK_LANGUAGE);
    return Optional.of(language);
  }
  private Optional<User> getOneUser(){
    return Optional.of(new User());
  }

}
