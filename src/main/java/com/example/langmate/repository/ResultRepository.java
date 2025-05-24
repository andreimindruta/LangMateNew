package com.example.langmate.repository;

import com.example.langmate.domain.entities.Language;
import com.example.langmate.domain.entities.Result;
import com.example.langmate.domain.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long>{

  List<Result> findAllByLanguageAndUserOrderByTimestamp(Language language, User user);

  @Query("SELECT AVG(r.grade) FROM Result r WHERE r.user.id = :userId AND r.language.id = :languageId")
  Double findAverageGradeByUserAndLanguage(@Param("userId") Long userId, @Param("languageId") Long languageId);

  @Query("SELECT COUNT(r) FROM Result r WHERE r.user.id = :userId AND r.language.id = :languageId")
  int countTestsForUserAndLanguage(@Param("userId") Long userId, @Param("languageId") Long languageId);


}
