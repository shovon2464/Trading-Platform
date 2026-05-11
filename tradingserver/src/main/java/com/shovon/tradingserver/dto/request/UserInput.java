package com.shovon.tradingserver.dto.request;


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
public class UserInput {

  private String userName;

  private String firstName;

  private String lastName;

  private String email;

  private String password;

}
