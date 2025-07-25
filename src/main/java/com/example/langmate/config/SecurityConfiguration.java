package com.example.langmate.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.example.langmate.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final UserService userService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            request ->
                request
                    .requestMatchers(
                        "/",
                        "/login",
                        "/documentation",
                        "/andrei-api-docs",
                        "/andrei-api-docs/*",
                        "/swagger-ui/*",
                        "/langmate/users/login",
                        "/langmate/users/register",
                        "/langmate/language/all",
                        "/langmate/language/all**",
                        "/langmate/language/all?**",
                        "/langmate/language/all/**",
                        "/langmate/language/enroll/*",
                        "/register-login",
                        "/langmate/tests",
                        "/langmate/tests/*",
                        "/langmate/tests/**",
                        "/langmate/tests/*/submit",
                        "/langmate/milestones/web",
                        "/langmate/milestones/create-web",
                        "/langmate/milestones/edit/*",
                        "/langmate/milestones/update-web/*",
                        "/langmate/milestones/delete-web/*"
                    )
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userService.userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}
