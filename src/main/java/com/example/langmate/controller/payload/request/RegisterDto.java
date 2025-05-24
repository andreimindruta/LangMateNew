package com.example.langmate.controller.payload.request;

import jakarta.validation.constraints.Size;
import lombok.NonNull;

public record RegisterDto(@NonNull @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters") String username,
                          @NonNull String pass,
                          @Size(min = 10, max = 100, message = "Name must be between 10 and 100 characters")
                          @NonNull String name) {}
