package com.example.langmate.controller;

import com.example.langmate.controller.payload.response.GetLanguagesResponse;
import com.example.langmate.service.impl.LanguageService;
import com.example.langmate.validation.LangmateRuntimeException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

@Controller
@RequestMapping("/langmate/language")
@Slf4j
@RequiredArgsConstructor
public class LanguagesController {

  private final LanguageService languageService;

  //  @GetMapping()
  //  public ResponseEntity<GetLanguagesResponse> getUserLanguages() throws LangmateRuntimeException
  // {
  //    return ResponseEntity.ok(languageService.getUserLanguages());
  //  }

  @GetMapping(path = "/all")
  public String getLanguages(Model model) throws LangmateRuntimeException {
    model.addAttribute("languages", languageService.getLanguages().languages());

    return "allLanguages";
  }

  @GetMapping(path = "/enroll/{languageName}")
  public String enroll(Model model) {
    return "enroll";
  }

  @PostMapping("/enroll/{languageName}")
  public String addLanguage(
      @PathVariable @NonNull String languageName,
      Model model,
      HttpServletRequest request,
      RedirectAttributes redirectAttributes) {
    try {
      Cookie jwtCookie = WebUtils.getCookie(request, "JWT");
      languageService.addLanguageToUser(languageName, jwtCookie.getValue());
      model.addAttribute("language", languageName);
      return "enroll"; // view page redirect
    } catch (LangmateRuntimeException ex) {
      redirectAttributes.addFlashAttribute("error", "A apărut o eroare: " + ex.getMessage());
      return "redirect:/langmate/language/all"; // redirect back if there's an exception -> to the form
    }
  }
}
