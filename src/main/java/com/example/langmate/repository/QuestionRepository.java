package com.example.langmate.repository;

import com.example.langmate.domain.entities.Language;
import com.example.langmate.domain.entities.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

  List<Question> findAllByLanguage(Language language);

  Question findByQuestion(String question);
}
