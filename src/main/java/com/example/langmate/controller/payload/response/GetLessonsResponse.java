package com.example.langmate.controller.payload.response;

import java.util.List;
import lombok.NonNull;

public record GetLessonsResponse(@NonNull List<GetLessonResponse> lessons) {

}
