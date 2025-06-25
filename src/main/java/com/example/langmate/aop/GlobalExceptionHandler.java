package com.example.langmate.aop;

import com.example.langmate.controller.payload.response.LangmateErrorResponse;
import com.example.langmate.validation.LangmateRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({LangmateRuntimeException.class})
  public ResponseEntity<LangmateErrorResponse> hanldeExceptions(LangmateRuntimeException e) {
    return ResponseEntity.status(e.getStatus()).body(new LangmateErrorResponse(e.getMessage()));
  }

  @ExceptionHandler({NullPointerException.class})
  public ResponseEntity<LangmateErrorResponse> hanldeNullExceptions(NullPointerException e) {
    return ResponseEntity.status(500).body(new LangmateErrorResponse(e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationErrors(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
    return ResponseEntity.badRequest().body(errors);
  }
}
