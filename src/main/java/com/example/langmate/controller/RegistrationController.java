package com.example.langmate.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegistrationController {

    @GetMapping("/register-login")
    public ModelAndView showRegisterLoginPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register-login");
        return modelAndView;
    }

}