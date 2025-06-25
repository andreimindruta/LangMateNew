package com.example.langmate.repository;

import com.example.langmate.domain.entities.Language;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

  Optional<Language> findByName(String name);
}
