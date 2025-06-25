package com.example.langmate.config;

import com.example.langmate.service.impl.UserService;
import com.example.langmate.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtUtils jwtService;
  private final UserService userService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    // Check Authorization header first
    val authHeader = request.getHeader("Authorization");
    String jwtToken = null;
    
    if (StringUtils.isNotEmpty(authHeader) && StringUtils.startsWith(authHeader, "Bearer ")) {
      jwtToken = authHeader.substring(7);
    } else {
      // If no Authorization header, check for JWT cookie
      val cookies = request.getCookies();
      if (cookies != null) {
        for (val cookie : cookies) {
          if ("JWT".equals(cookie.getName())) {
            jwtToken = cookie.getValue();
            break;
          }
        }
      }
    }
    
    if (StringUtils.isEmpty(jwtToken)) {
      filterChain.doFilter(request, response);
      return;
    }
    
    val username = jwtService.getUserNameFromJwtToken(jwtToken);
    if (StringUtils.isNotEmpty(username)
        && SecurityContextHolder.getContext().getAuthentication() == null) {
      val userDetails = userService.loadJwtUser(username);
      if (jwtService.validateJwtToken(jwtToken)) {
        val context = SecurityContextHolder.createEmptyContext();
        val authToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
      }
    }
    filterChain.doFilter(request, response);
  }
}
