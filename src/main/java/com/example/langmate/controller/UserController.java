package com.example.langmate.controller;

import com.example.langmate.controller.payload.request.LoginDto;
import com.example.langmate.controller.payload.request.RegisterDto;
import com.example.langmate.service.impl.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/langmate/users")
public class UserController {

  @Autowired private UserService userService;

  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    model.addAttribute("registerDto", new RegisterDto("", "", ""));
    return "register";
  }

  @PostMapping("/register")
  public String registerUser(
      @ModelAttribute @Valid RegisterDto registerDto, 
      BindingResult result, 
      Model model,
      HttpServletResponse response) {
    if (result.hasErrors()) {
      // Return the form view if there are errors
      return "register";
    }
    
    // Register the user
    userService.register(registerDto);
    
    // Automatically log in the user after registration
    LoginDto loginDto = new LoginDto(registerDto.username(), registerDto.pass());
    String jwt = userService.processLogin(loginDto);
    
    if (jwt != null) {
      // Create a cookie and set the JWT as its value
      Cookie jwtCookie = new Cookie("JWT", jwt);
      jwtCookie.setHttpOnly(true);
      jwtCookie.setPath("/");
      jwtCookie.setSecure(true); // Set secure to true if using HTTPS
      response.addCookie(jwtCookie);
      
      // Redirect to languages page with authenticated user
      return "redirect:/langmate/language/all";
    } else {
      // If auto-login fails, redirect to login page
      model.addAttribute("message", "User registered successfully. Please log in.");
      return "redirect:/langmate/users/login";
    }
  }

  @GetMapping("/login")
  public String showLoginForm(Model model) {
    model.addAttribute("loginDto", new LoginDto("", ""));
    return "login"; // login.html Thymeleaf template
  }

  @PostMapping("/login")
  public String loginUser(
      @Valid @ModelAttribute("loginDto") LoginDto loginDto,
      BindingResult result,
      Model model,
      HttpServletResponse response) {
    if (result.hasErrors()) {
      return "login";
    }

    String jwt = userService.processLogin(loginDto);
    
    // Check if login was successful (JWT is not null)
    if (jwt == null) {
      model.addAttribute("error", "Invalid username or password");
      model.addAttribute("loginDto", loginDto);
      return "login";
    }
    
    // Create a cookie and set the JWT as its value
    Cookie jwtCookie = new Cookie("JWT", jwt);
    jwtCookie.setHttpOnly(true);
    jwtCookie.setPath("/");
    jwtCookie.setSecure(true); // Set secure to true if using HTTPS
    response.addCookie(jwtCookie);

    return "redirect:/langmate/language/all"; // Redirect to a secure page
  }
}
