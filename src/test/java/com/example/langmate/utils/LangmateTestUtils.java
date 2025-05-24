package com.example.langmate.utils;

import com.example.langmate.domain.entities.User;

import java.util.Optional;

public class LangmateTestUtils {
    public static Optional<User> mockNewUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Andrei");
        user.setPass("andrei");
        user.setUsername("andrei");

        return Optional.of(user);
    }
}
