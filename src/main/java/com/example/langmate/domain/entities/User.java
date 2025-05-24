package com.example.langmate.domain.entities;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String username;
    private String pass;

    @ManyToMany
    @JoinTable(
            name = "interest",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<Language> languages;

    // Constructor pentru ID
    public User(Long id) {
        this.id = id;
    }

    // Constructor implicit (necesar pentru JPA)
    public User() {}
}
