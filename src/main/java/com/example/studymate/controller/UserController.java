package com.example.studymate.controller;

import com.example.studymate.dto.User.*;
import com.example.studymate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    final UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserRequestDto dto) {
            userService.signup(dto);
            return ResponseEntity.ok("회원가입 성공 !");
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody UserRequestDto dto) {

        return ResponseEntity.ok(userService.login(dto));

    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequestDto dto) {

        if(userService.logout(dto))
            return ResponseEntity.ok("로그아웃 성공 !");
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
    }


    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePwDto dto) {

        userService.changePassword(dto);
        return ResponseEntity.ok("비밀번호 변경이 완료되었습니다.");
    }


    @PostMapping("/findUsername")
    public ResponseEntity<FindUsernameResponseDto> findUsername(@RequestBody FindUsernameRequestDto dto) {
        return ResponseEntity.ok(userService.findUsername(dto));
    }
}
