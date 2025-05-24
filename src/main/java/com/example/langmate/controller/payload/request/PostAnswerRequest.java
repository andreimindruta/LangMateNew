package com.example.langmate.controller.payload.request;

import jakarta.validation.constraints.Size;
import lombok.NonNull;

public record PostAnswerRequest(@NonNull @Size(min = 6, max = 100, message = "Question must be between 6 and 100 characters")
                                String question, @NonNull String answer) {

}
