package com.example.langmate.controller.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MilestoneRequest(
        @NotBlank(message = "Milestone name cannot be blank")
        String name,

        @NotBlank(message = "Milestone description cannot be blank")
        String description,

        @NotNull(message = "Target value must not be null")
        @Positive(message = "Target value must be a positive number")
        Integer targetValue,

        @NotBlank(message = "Target type cannot be blank")
        String targetType
) {
}
