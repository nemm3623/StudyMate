package com.example.studymate.service;

import com.example.studymate.domain.User;
import com.example.studymate.dto.UserDto;
import com.example.studymate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public void signup(UserDto dto) {

        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        // 아이디, 이메일 중복 확인
        if (userRepository.existsByUsername(dto.getUsername()))
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        if(userRepository.existsByEmail(dto.getEmail()))
            throw new IllegalStateException("이미 존재하는 이메일입니다.");

        User user = User.builder()
                .username(dto.getUsername())
                .password(hashedPassword)
                .email(dto.getEmail()).build();

        userRepository.save(user);
    }




}
