package com.example.langmate.domain.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;


@Entity
@Data
@Builder
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String text;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    public Lesson() {
    }

    public Lesson(Long id, String title, String text, Language language) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.language = language;
    }
}