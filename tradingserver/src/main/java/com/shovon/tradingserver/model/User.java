package com.shovon.tradingserver.model;


import com.shovon.tradingserver.types.UserGenderType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String userName;

  private String firstName;

  private String lastName;

  @Column(nullable = false, unique = true)
  private String email;

  private String password;

  private String loginPin;

  private String phoneNumber;

  private LocalDate birthday;

  private String biometricKey;

  @Enumerated(EnumType.STRING)
  private UserGenderType gender;

  private int wrongPinAttempts;

  private LocalDateTime blockedUntilPin;

  private int wrongPasswordAttempts;

  private LocalDateTime blockedUntilPassword;

  private Long balance;

  private LocalDateTime createdDate;

  private LocalDateTime updatedDate;


}
