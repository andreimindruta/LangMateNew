package com.example.langmate.controller;

import com.example.langmate.controller.payload.request.GetQuestionRequest;
import com.example.langmate.controller.payload.request.PostTestResultRequest;
import com.example.langmate.controller.payload.response.GetQuestionsResponse;
import com.example.langmate.controller.payload.request.PostAnswerRequest;
import com.example.langmate.controller.payload.response.GetResultResponse;
import com.example.langmate.service.impl.TestService;
import com.example.langmate.service.impl.MilestoneService;
import com.example.langmate.service.impl.UserService;
import com.example.langmate.domain.entities.MilestoneUser;
import com.example.langmate.domain.entities.User;
import com.example.langmate.domain.entities.Milestone;
import com.example.langmate.validation.LangmateRuntimeException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/langmate/tests")
@RequiredArgsConstructor
@Slf4j
public class TestController {

  private final TestService testService;
  private final MilestoneService milestoneService;
  private final UserService userService;

  @GetMapping
  public String showTestPage(Model model) {
    // For now, just show the test page template
    return "test";
  }

  @PostMapping(path = "/questions")
  public ResponseEntity<GetQuestionsResponse> findQuestionsForTest(@RequestBody @NonNull final
      GetQuestionRequest request) throws LangmateRuntimeException {
    return ResponseEntity.ok(testService.findQuestionsForTest(request));
  }

  @PostMapping(path = "/answers")
  public ResponseEntity<GetResultResponse> saveResultForTest(@RequestBody @NonNull final
  PostTestResultRequest request, @RequestHeader("Authorization") String authorization) throws LangmateRuntimeException {
    String jwt = authorization.replace("Bearer ", "");
    return ResponseEntity.ok(testService.saveResultForTest(request, jwt));
  }

  @GetMapping("/{languageName}")
  public String showTestForLanguage(@PathVariable String languageName, Model model) throws com.example.langmate.validation.LangmateRuntimeException {
    log.info("Received request for test with language: '{}'", languageName);
    
    // Obține întrebările pentru limba respectivă
    List<String> questions = testService.getQuestionsForLanguage(languageName);
    
    log.info("Found {} questions for language: '{}'", questions.size(), languageName);
    
    model.addAttribute("language", languageName);
    model.addAttribute("questions", questions);
    return "test";
  }

  @PostMapping("/{languageName}/submit")
  public String submitTestForLanguage(@PathVariable String languageName, 
                                     @RequestParam("answers") java.util.List<String> answers,
                                     @RequestParam("questions") java.util.List<String> questions,
                                     Model model, 
                                     Principal principal) throws com.example.langmate.validation.LangmateRuntimeException {
    // DEBUG LOGS
    System.out.println("[DEBUG] Principal: " + principal);
    System.out.println("[DEBUG] Language: " + languageName);
    System.out.println("[DEBUG] Number of answers received: " + answers.size());
    System.out.println("[DEBUG] Answers: " + answers);
    System.out.println("[DEBUG] Number of questions received: " + questions.size());
    System.out.println("[DEBUG] Questions: " + questions);
    
    // Verifică dacă utilizatorul este autentificat
    if (principal == null) {
      model.addAttribute("error", "Nu esti autentificat! Trebuie sa fii logat pentru a trimite testul.");
      return "register-login";
    }
    
    // Folosim întrebările primite din formular în loc să le obținem din nou din baza de date
    System.out.println("[DEBUG] Using questions from form: " + questions);
    System.out.println("[DEBUG] Number of questions from form: " + questions.size());

    
    // Salvează rezultatul folosind logica existentă din saveResultForTest
    // Convertim răspunsurile în formatul PostAnswerRequest pentru a folosi logica existentă
    List<PostAnswerRequest> answerRequests = IntStream.range(0, Math.min(questions.size(), answers.size()))
        .mapToObj(i -> new PostAnswerRequest(questions.get(i), answers.get(i)))
        .collect(Collectors.toList());
    
    // Creăm un PostTestResultRequest pentru a folosi logica existentă
    PostTestResultRequest testResultRequest = new PostTestResultRequest(answerRequests, languageName);
    
    // Obținem JWT token-ul din principal (username)
    String username = principal.getName();
    // Folosim UserService pentru a obține JWT token-ul
    String jwt = testService.getJwtForUser(username);
    
    // Salvăm rezultatul folosind logica existentă
    GetResultResponse result = testService.saveResultForTest(testResultRequest, jwt);
    
    // Obținem milestone-urile utilizatorului
    Optional<User> userOpt = userService.getUserByUsername(username);
    if (userOpt.isPresent()) {
        User user = userOpt.get();
        List<Milestone> userMilestones = milestoneService.getUserMilestones(user.getId());
        model.addAttribute("userMilestones", userMilestones);
    } else {
        model.addAttribute("userMilestones", List.of());
    }
    
    // Adăugăm datele în model pentru view
    model.addAttribute("score", result.grade());
    model.addAttribute("language", result.language());
    model.addAttribute("timestamp", result.timestamp());
    
    return "testResult";
  }

}
