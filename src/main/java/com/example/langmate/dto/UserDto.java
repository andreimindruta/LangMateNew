package com.example.langmate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {

    @NotBlank(message = "Username-ul este obligatoriu")
    private String username;

    @NotBlank(message = "Parola este obligatorie")
    private String pass;

    @NotBlank(message = "Numele este obligatoriu")
    private String name;

    @Email(message = "Emailul nu este valid")
    private String email;
}
