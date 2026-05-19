package com.shovon.tradingserver.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

  private String accessToken;

  private String refreshToken;

  private String id;

  private String fullName;

  private String email;

  private Boolean phoneExist;

  private Boolean isTwoFactorEnabled;

  private String message;


}
