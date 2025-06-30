package com.example.studymate.dto;


import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    // User 객체에 직접 접근하는 것을 막기 위해 사용

    private String username;
    private String password;
    private String email;
}
