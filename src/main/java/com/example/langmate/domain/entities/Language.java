package com.example.langmate.domain.entities;

import jakarta.persistence.*;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder(toBuilder = true)
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "language")
    private List<Question> questions;

    @OneToMany(mappedBy = "language")
    private List<Lesson> lessons;

    public Language() {
    }

    public Language(Long id, String name, List<Question> questions, List<Lesson> lessons) {
        this.id = id;
        this.name = name;
        this.questions = questions;
        this.lessons = lessons;
    }
}