package com.shovon.tradingserver.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.shovon.tradingserver.types.UserGenderType;
import com.shovon.tradingserver.types.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

  private String fullName;

  @Column(nullable = false, unique = true)
  private String email;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private UserRole userRole = UserRole.CUSTOMER;

  private String phoneNumber;

  @Enumerated(EnumType.STRING)
  private UserGenderType gender;

  @Builder.Default
  private Boolean isTwoFactorEnabled = false;

  private LocalDateTime createdDate;

  private LocalDateTime updatedDate;


}
