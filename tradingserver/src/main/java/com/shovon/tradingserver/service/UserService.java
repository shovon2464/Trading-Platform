package com.shovon.tradingserver.service;

import com.shovon.tradingserver.dto.request.UserInput;
import com.shovon.tradingserver.dto.response.LoginResponse;
import com.shovon.tradingserver.model.User;
import com.shovon.tradingserver.repository.UserRepository;
import com.shovon.tradingserver.security.JwtUtil;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtUtil jwtUtil;


  public LoginResponse create(UserInput userInput) {
    if (userInput.getEmail() == null) {
      throw new IllegalArgumentException("Email is required");
    }
    String email = userInput.getEmail();
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    if (!email.matches(emailRegex)) {
        throw new IllegalArgumentException("Invalid email format");
    }
    if (userInput.getPassword() == null || userInput.getPassword().isBlank()) {
      throw new IllegalArgumentException("Password is required");
    }

    Optional<User> optUser = this.userRepository.findByEmail(userInput.getEmail());

    if (optUser.isPresent()) {
      throw new RuntimeException("Email already exists");
    }

    String encode = BCrypt.hashpw(userInput.getPassword(), BCrypt.gensalt(12));

    User user = User.builder()
        .userName(userInput.getUserName())
        .firstName(userInput.getFirstName())
        .lastName(userInput.getLastName())
        .email(userInput.getEmail())
        .password(encode)
        .build();

    User savedUser = this.userRepository.save(user);
    String accessToken = this.jwtUtil.generateAccessToken(savedUser.getEmail());
    String refreshToken = this.jwtUtil.generateRefreshToken(savedUser.getEmail());


    return LoginResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .id(savedUser.getId().toString())
        .userName(savedUser.getUserName())
        .email(savedUser.getEmail())
        .build();

  }

  public LoginResponse login(UserInput userInput) {

    if (userInput.getEmail() == null) {
      throw new IllegalArgumentException("Email is required");
    }
    String email = userInput.getEmail();
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    if (!email.matches(emailRegex)) {
        throw new IllegalArgumentException("Invalid email format");
    }

    if (userInput.getPassword() == null || userInput.getPassword().isBlank()) {
      throw new IllegalArgumentException("Password is required");
    }
    Optional<User> optUser = this.userRepository.findByEmail(userInput.getEmail());
    if (optUser.isEmpty()) {
      throw new RuntimeException("User Not Found");
    }
    User user = optUser.get();
    if (user.getPassword() == null || user.getPassword().isBlank()) {
      throw new RuntimeException("Password is required");
    }
    if (!BCrypt.checkpw(userInput.getPassword(), user.getPassword())) {
      throw new RuntimeException("Invalid password");
    }
    String accessToken = this.jwtUtil.generateAccessToken(user.getEmail());
    String refreshToken = this.jwtUtil.generateRefreshToken(user.getEmail());

    return LoginResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .id(user.getId().toString())
        .userName(user.getUserName())
        .email(user.getEmail())
        .build();
  }

}
