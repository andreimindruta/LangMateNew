package com.example.langmate.controller.payload.response;

import lombok.NonNull;

public record GetMilestoneResponse(
        @NonNull Long id,
        @NonNull String name,
        @NonNull String description,
        @NonNull Integer targetValue,
        @NonNull String targetType
) {
}
