package com.example.studymate.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor  // 기본 생성자 생성
@AllArgsConstructor // 모든 매개변수를 갖는 생성자 생성
@Builder
public class User {

    // PRIMARY KEY 지정 및 AUTO INCREMENT 지정
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

}
