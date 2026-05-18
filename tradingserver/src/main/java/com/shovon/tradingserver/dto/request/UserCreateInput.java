package com.shovon.tradingserver.dto.request;


import com.shovon.tradingserver.types.UserGenderType;
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
public class UserCreateInput {

  private String email;

  private String password;

  private String fullName;

  private String phoneNumber;

  private UserGenderType gender;

}
