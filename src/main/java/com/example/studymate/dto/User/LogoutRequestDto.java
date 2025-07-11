package com.example.studymate.dto.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequestDto {

    private String refreshToken;

}
