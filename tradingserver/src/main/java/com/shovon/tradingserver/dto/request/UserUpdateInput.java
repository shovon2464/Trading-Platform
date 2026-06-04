package com.shovon.tradingserver.dto.request;


import com.shovon.tradingserver.types.UserGenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateInput {

  private String fullName;

  private UserGenderType gender;


}
