package com.shovon.tradingserver.dto.response;

import com.shovon.tradingserver.model.User;
import com.shovon.tradingserver.types.UserGenderType;
import com.shovon.tradingserver.types.UserRole;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummary {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private UserRole userRole;
    private UserGenderType gender;
    private Boolean isTwoFactorEnabled;
    private Double balance;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static UserSummary fromUser(User user) {
        return UserSummary.builder()
            .id(user.getId())
            .fullName(user.getFullName())
            .email(user.getEmail())
            .phoneNumber(user.getPhoneNumber())
            .userRole(user.getUserRole())
            .gender(user.getGender())
            .isTwoFactorEnabled(user.getIsTwoFactorEnabled())
            .balance(user.getBalance())
            .createdDate(user.getCreatedDate())
            .updatedDate(user.getUpdatedDate())
            .build();
    }
}