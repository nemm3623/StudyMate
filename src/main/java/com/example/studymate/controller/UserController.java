package com.example.studymate.controller;

import com.example.studymate.dto.UserDto;
import com.example.studymate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDto dto) {

        try {
            userService.signup(dto);
            return ResponseEntity.ok("회원가입 성공 !");
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
