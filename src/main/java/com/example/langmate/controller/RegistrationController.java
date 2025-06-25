package com.example.langmate.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistrationController {

    @GetMapping("/register-login")
    public String showRegisterLoginPage() {
        return "register-login";
    }

}