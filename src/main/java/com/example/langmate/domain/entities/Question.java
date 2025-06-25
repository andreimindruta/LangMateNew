package com.example.langmate.domain.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class Question {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String question;
  private String answer;

  @ManyToOne
  @JoinColumn(name = "language_id")
  private Language language;

  public Question() {}

  public Question(Long id, String question, String answer, Language language) {
    this.id = id;
    this.question = question;
    this.answer = answer;
    this.language = language;
  }
}
