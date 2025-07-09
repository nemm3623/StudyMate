package com.example.studymate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindUsernameRequestDto {
    private String nickname;
    private String email;
}
