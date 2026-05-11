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

  private String userName;

  private String email;

}
