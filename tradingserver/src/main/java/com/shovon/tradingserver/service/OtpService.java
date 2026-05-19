package com.shovon.tradingserver.service;


import com.shovon.tradingserver.model.Otp;
import com.shovon.tradingserver.model.User;
import com.shovon.tradingserver.repository.OtpRepository;
import com.shovon.tradingserver.repository.UserRepository;
import com.shovon.tradingserver.types.OtpType;
import com.shovon.tradingserver.utils.TimeUtils;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

  @Autowired
  private EmailService emailService;

  @Autowired
  private OtpRepository otpRepository;

  @Autowired
  private UserRepository userRepository;


  @Transactional
  public Otp createOtp(String email, String otpCode, OtpType otpType) {
    Otp newOtp = Otp.builder()
        .email(email)
        .otpCode(otpCode)
        .otpType(otpType)
        .createdDate(TimeUtils.nowUtc())
        .build();
    return this.otpRepository.save(newOtp);
  }

  public boolean verifyOtp(String email, String otp, OtpType otpType) {
    Optional<Otp> optOtp = this.otpRepository.findTopByEmailAndOtpTypeOrderByCreatedDateDesc(email, otpType);

    if (optOtp.isEmpty()) {
      throw new RuntimeException("OTP not found");
    }

    Otp foundOtp = optOtp.get();

    if (foundOtp.getOtpCode() != null && foundOtp.getOtpCode().equals(otp)) {
      this.otpRepository.delete(foundOtp);
      return true;
    } else {
      throw new RuntimeException("Invalid OTP");
    }
  }

  @Transactional
  public void sendOtp(String email, OtpType otpType) {

    Optional<Otp> optOtp = this.otpRepository.findTopByEmailAndOtpTypeOrderByCreatedDateDesc(email, otpType);

    if (optOtp.isPresent()) {
      Otp foundOtp = optOtp.get();
      if (!this.isExpired(foundOtp.getCreatedDate())) {
        throw new RuntimeException("Wait for 30 seconds before sending another OTP");
      } else {
        this.otpRepository.delete(foundOtp);
      }
    }

    int OTP_LENGTH = 6;

    Random random = new Random();
    StringBuilder otpCodeSB = new StringBuilder();
    for (int i = 0; i < OTP_LENGTH; i++) {
      otpCodeSB.append(random.nextInt(10));
    }
    String otpCode = otpCodeSB.toString();

    Optional<User> optUser = this.userRepository.findByEmail(email);

    if (optUser.isEmpty()) {
      throw new RuntimeException("User Not Found");
    }

    Otp newOtp = this.createOtp(email, otpCode, otpType);
    try {
      this.emailService.sendOtpEmail(email, otpCode, otpType.name());
    } catch (Exception ex) {
      throw new RuntimeException("Failed to send OTP email", ex);
    }

  }

  public boolean isExpired(LocalDateTime createdDate) {
    if (createdDate == null) {
      return true;
    }

    LocalDateTime expiryTime = createdDate.plusSeconds(30);

    return LocalDateTime.now().isAfter(expiryTime);
  }

}
