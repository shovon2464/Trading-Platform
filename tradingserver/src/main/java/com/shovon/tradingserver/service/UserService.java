package com.shovon.tradingserver.service;

import com.shovon.tradingserver.dto.request.UserCreateInput;
import com.shovon.tradingserver.dto.request.UserLoginInput;
import com.shovon.tradingserver.dto.request.UserUpdateInput;
import com.shovon.tradingserver.dto.response.LoginResponse;
import com.shovon.tradingserver.model.User;
import com.shovon.tradingserver.repository.UserRepository;
import com.shovon.tradingserver.security.JwtUtil;
import com.shovon.tradingserver.types.OtpType;
import com.shovon.tradingserver.utils.TimeUtils;
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

  @Autowired
  private OtpService otpService;


  public Boolean checkEmail(String email) {
    if (email == null) {
      throw new IllegalArgumentException("Email is required");
    }
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    if (!email.matches(emailRegex)) {
        throw new IllegalArgumentException("Invalid email format");
    }
    Optional<User> optUser = this.userRepository.findByEmail(email);
    if (optUser.isPresent()) {
      return true;
    }

    //this.otpService.sendOtp(email, OtpType.CREATE_ACCOUNT);
    return false;
  }


  public LoginResponse create(UserCreateInput userCreateInput) {
    if (userCreateInput.getEmail() == null) {
      throw new IllegalArgumentException("Email is required");
    }
    String email = userCreateInput.getEmail();
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    if (!email.matches(emailRegex)) {
        throw new IllegalArgumentException("Invalid email format");
    }

    if (userCreateInput.getPassword() == null || userCreateInput.getPassword().isBlank()) {
      throw new IllegalArgumentException("Password is required");
    }

    Optional<User> optUser = this.userRepository.findByEmail(userCreateInput.getEmail());

    if (optUser.isPresent()) {
      throw new RuntimeException("User with this Email already exists");
    }


    String encode = BCrypt.hashpw(userCreateInput.getPassword(), BCrypt.gensalt(12));


    User user = User.builder()
        .fullName(userCreateInput.getFullName())
        .email(email)
        .password(encode)
        .phoneNumber(userCreateInput.getPhoneNumber())
        .gender(userCreateInput.getGender())
        .createdDate(TimeUtils.nowUtc())
        .build();

    User savedUser = this.userRepository.save(user);
    String accessToken = this.jwtUtil.generateAccessToken(savedUser.getEmail());
    String refreshToken = this.jwtUtil.generateRefreshToken(savedUser.getEmail());


    return LoginResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .id(savedUser.getId().toString())
        .fullName(savedUser.getFullName())
        .email(savedUser.getEmail())
        .phoneExist(savedUser.getPhoneNumber() != null)
        .isTwoFactorEnabled(savedUser.getIsTwoFactorEnabled())
        .message("Account Created Successfully")
        .build();

  }

  public Boolean update(UserUpdateInput userUpdateInput, String email) {
    if (email == null) {
      throw new IllegalArgumentException("Email is required");
    }
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    if (!email.matches(emailRegex)) {
        throw new IllegalArgumentException("Invalid email format");
    }

    Optional<User> optUser = this.userRepository.findByEmail(email);

    if (optUser.isEmpty()) {
      throw new RuntimeException("User with this Email doesn't exists");
    }

    User user = optUser.get();
    user.setFullName(userUpdateInput.getFullName());
    user.setGender(userUpdateInput.getGender());
    user.setUpdatedDate(TimeUtils.nowUtc());


    User savedUser = this.userRepository.save(user);
    return true;

  }

  public LoginResponse login(UserLoginInput userInput) {

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

    if (user.getIsTwoFactorEnabled()) {
      this.otpService.sendOtp(user.getEmail(), OtpType.LOGIN);
      return LoginResponse.builder()
          .id(user.getId().toString())
          .fullName(user.getFullName())
          .email(user.getEmail())
          .phoneExist(user.getPhoneNumber() != null)
          .isTwoFactorEnabled(true)
          .message("Two Factor Authentication Required")
          .build();
    }
    String accessToken = this.jwtUtil.generateAccessToken(user.getEmail());
    String refreshToken = this.jwtUtil.generateRefreshToken(user.getEmail());

    return LoginResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .id(user.getId().toString())
        .fullName(user.getFullName())
        .email(user.getEmail())
        .phoneExist(user.getPhoneNumber() != null)
        .isTwoFactorEnabled(user.getIsTwoFactorEnabled())
        .message("Login Successful")
        .build();
  }

  public LoginResponse verifyLoginOtp(String email, String otp) {
    if (email == null) {
      throw new IllegalArgumentException("Email is required");
    }

    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    if (!email.matches(emailRegex)) {
      throw new IllegalArgumentException("Invalid email format");
    }

    try {
      this.otpService.verifyOtp(email, otp, OtpType.LOGIN);
      Optional<User> optUser = this.userRepository.findByEmail(email);
      if (optUser.isEmpty()) {
        throw new RuntimeException("User Not Found");
      }
      User user = optUser.get();

      String accessToken = this.jwtUtil.generateAccessToken(user.getEmail());
      String refreshToken = this.jwtUtil.generateRefreshToken(user.getEmail());

      return LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
          .id(user.getId().toString()).fullName(user.getFullName()).email(user.getEmail())
          .phoneExist(user.getPhoneNumber() != null)
          .isTwoFactorEnabled(user.getIsTwoFactorEnabled()).message("Two Factor Authentication Verified").build();

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }


  public LoginResponse refreshLogin(String refreshToken) {
    if (refreshToken == null || refreshToken.isBlank()) {
      throw new IllegalArgumentException("Refresh token is required");
    }

    this.jwtUtil.validateToken(refreshToken);
    String email = jwtUtil.getEmailFromToken(refreshToken);

    Optional<User> optUser = this.userRepository.findByEmail(email);
    if (optUser.isEmpty()) {
      throw new RuntimeException("User Not Found");
    }

    User user = optUser.get();

    String newAccessToken = this.jwtUtil.generateAccessToken(user.getEmail());
    String newRefreshToken = this.jwtUtil.generateRefreshToken(user.getEmail());

    return LoginResponse.builder().accessToken(newAccessToken).refreshToken(newRefreshToken)
        .id(user.getId().toString()).fullName(user.getFullName()).email(user.getEmail())
        .phoneExist(user.getPhoneNumber() != null)
        .build();
  }


  public Boolean enableTwoFactorAuthentication(String email) {
    Optional<User> optUser = this.userRepository.findByEmail(email);
    if (optUser.isEmpty()) {
      throw new RuntimeException("User Not Found");
    }
    User user = optUser.get();
    user.setIsTwoFactorEnabled(true);
    this.userRepository.save(user);
    return true;
  }

  public Boolean disableTwoFactorAuthentication(String email) {
    Optional<User> optUser = this.userRepository.findByEmail(email);
    if (optUser.isEmpty()) {
      throw new RuntimeException("User Not Found");
    }
    User user = optUser.get();
    user.setIsTwoFactorEnabled(false);
    this.userRepository.save(user);
    return true;
  }

  public Boolean updatePassword(String email, String newPassword) {
    Optional<User> optUser = this.userRepository.findByEmail(email);

    if (optUser.isEmpty()) {
      throw new RuntimeException("User Not Found");
    }

    User user = optUser.get();
    String encode = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
    user.setPassword(encode);
    this.userRepository.save(user);
    return true;
  }


  public LoginResponse getProfile(String email) {
    Optional<User> optUser = this.userRepository.findByEmail(email);

    User user = optUser.get();

    String newAccessToken = this.jwtUtil.generateAccessToken(user.getEmail());
    String newRefreshToken = this.jwtUtil.generateRefreshToken(user.getEmail());

    return LoginResponse.builder().accessToken(newAccessToken).refreshToken(newRefreshToken)
        .id(user.getId().toString()).fullName(user.getFullName()).email(user.getEmail())
        .phoneExist(user.getPhoneNumber() != null)
        .build();
  }


}
