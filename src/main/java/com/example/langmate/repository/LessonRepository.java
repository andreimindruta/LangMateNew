package com.example.langmate.repository;

import com.example.langmate.domain.entities.Language;
import com.example.langmate.domain.entities.Lesson;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

  List<Lesson> findAllByLanguage(Language language);
}