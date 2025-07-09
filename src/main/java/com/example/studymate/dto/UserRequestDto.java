package com.example.studymate.dto;


import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    // User 객체에 직접 접근하는 것을 막기 위해 사용

    private String username;
    private String email;
    private String password;
    private String nickname;
}
