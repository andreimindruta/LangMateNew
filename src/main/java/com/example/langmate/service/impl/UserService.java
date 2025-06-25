package com.example.langmate.service.impl;

import com.example.langmate.controller.payload.request.LoginDto;
import com.example.langmate.controller.payload.request.RegisterDto;
import com.example.langmate.domain.entities.User;
import com.example.langmate.mapper.RegisterDtoToUser;
import com.example.langmate.repository.UserRepository;
import com.example.langmate.utils.JwtUtils;

import java.util.HashSet;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final JwtUtils jwtUtils;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private RegisterDtoToUser registerMapper = Mappers.getMapper(RegisterDtoToUser.class);

  public String processLogin(LoginDto loginUser) {
    Optional<User> foundUser = userRepository.findByUsername(loginUser.username());
    User userData = foundUser.get();
    if (!passwordEncoder.matches(loginUser.password(), userData.getPass())) {
      log.debug("The password doesn't match for user {}", loginUser.username());
      return null;
    }
    return jwtUtils.generateJwtToken(userData);
  }

  public void register(RegisterDto registerDto) {
    User user = registerMapper.registerDtoToUser(registerDto);
    user.setPass(passwordEncoder.encode(registerDto.pass()));
    userRepository.save(user);
  }

  public Optional<User> getCurrentUser(String jwt) {
    String username = jwtUtils.getUserNameFromJwtToken(jwt);
    return userRepository.findByUsername(username);
  }

  public UserDetails loadJwtUser(String username) {
    val user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    val grantedAuthorities = new HashSet<SimpleGrantedAuthority>();
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(), user.getPass(), grantedAuthorities);
  }

  public UserDetailsService userDetailsService() {
    return new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(String username) {
        return loadJwtUser(username);
      }
    };
  }
}
