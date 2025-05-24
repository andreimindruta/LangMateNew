package com.example.langmate.domain.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder(toBuilder = true)
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double grade;
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    public Result(Long id, Double grade, Date timestamp, User user, Language language) {
        this.id = id;
        this.grade = grade;
        this.timestamp = timestamp;
        this.user = user;
        this.language = language;
    }

    public Result(){}
}