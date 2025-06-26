package com.example.langmate.repository;

import com.example.langmate.domain.entities.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
  Optional<Language> findByName(String name);
  
  Page<Language> findAll(Pageable pageable);
}
