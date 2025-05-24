package com.example.langmate.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LangmateRuntimeException extends Exception {

  private final int status;
  private final String message;
}
