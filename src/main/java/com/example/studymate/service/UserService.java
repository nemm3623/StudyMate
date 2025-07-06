package com.example.studymate.service;

import com.example.studymate.dto.LogoutRequestDto;
import com.example.studymate.security.JwtTokenProvider;
import com.example.studymate.domain.User;
import com.example.studymate.dto.LoginResponseDto;
import com.example.studymate.dto.UserRequestDto;
import com.example.studymate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    // 회원가입
    @Transactional
    public void signup(UserRequestDto dto) {

        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        // 아이디, 이메일 중복 확인
        if (userRepository.existsByUsername(dto.getUsername()))
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        if(userRepository.existsByEmail(dto.getEmail()))
            throw new IllegalStateException("이미 존재하는 이메일입니다.");

        User user = User.builder()
                .username(dto.getUsername())
                .password(hashedPassword)
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .build();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(UserRequestDto dto) {

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("아이디 혹은 패스워드가 일치하지 않습니다."));

        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("아이디 혹은 패스워드가 일치하지 않습니다.");

        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());

        redisService.set("refreshToken:" + user.getUsername(), refreshToken, 300);

        return new LoginResponseDto(accessToken, refreshToken);
    }

    // 로그아웃
    @Transactional
    public boolean logout(LogoutRequestDto dto) {

        String username = jwtTokenProvider.getUsername(dto.getRefreshToken());

        return redisService.delete("refreshToken:" + username);

    }
}
