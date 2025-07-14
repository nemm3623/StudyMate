package com.example.studymate.service;

import com.example.studymate.domain.*;
import com.example.studymate.dto.User.*;
import com.example.studymate.exception.AuthFailedException;
import com.example.studymate.exception.InvalidPasswordException;
import com.example.studymate.exception.ErrorCode;
import com.example.studymate.exception.UserNotFoundException;
import com.example.studymate.repository.*;
import com.example.studymate.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StudyGroupUserRepository studyGroupUserRepository;
    private final JoinStudyGroupRepository joinStudyGroupRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final StudyGroupUserService studyGroupUserService;


    // 회원가입
    @Transactional
    public void signup(UserRequestDto dto) {

        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        // 아이디, 이메일 중복 확인
        if (userRepository.existsByUsername(dto.getUsername()))
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        if(userRepository.existsByEmail(dto.getEmail()))
            throw new IllegalStateException("이미 존재하는 이메일입니다.");

        Role role = roleRepository.findByName("USER");

        User user = User.builder()
                .username(dto.getUsername())
                .password(hashedPassword)
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .roles(Set.of(role))
                .build();

        userRepository.save(user);
    }


    @Transactional(readOnly = true)
    public LoginResponseDto login(UserRequestDto dto) {

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new AuthFailedException(ErrorCode.AUTH_FAILED));

        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new AuthFailedException(ErrorCode.AUTH_FAILED);

        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());

        redisService.set("refreshToken:" + user.getUsername(), refreshToken, 604800);

        return new LoginResponseDto(accessToken, refreshToken);
    }


    // 로그아웃
    @Transactional
    public boolean logout(LogoutRequestDto dto) {

        String username = jwtTokenProvider.getUsername(dto.getRefreshToken());

        return redisService.delete("refreshToken:" + username);

    }


    // 비밀번호 변경
    @Transactional
    public void changePassword(ChangePwDto dto) {

        // 현재 스레드의 SecurityContext 가져오고 안에 저장된 Authentication 객체를 가져옴
        // jwt토큰이 필터에서 인증정보로 변환되어 Authentication 객체가 되어 SecurityContext 저장됨
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        if(!passwordEncoder.matches(dto.getCurrentPw(), user.getPassword()))
            throw new InvalidPasswordException(ErrorCode.INVALID_PASSWORD);

        userRepository.updateByPassword(username, passwordEncoder.encode(dto.getNewPw()));

    }


    // 아이디 찾기
    @Transactional
    public FindUsernameResponseDto findUsername(FindUsernameRequestDto dto) {

        User user = userRepository.findByNicknameAndEmail(dto.getNickname(),dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        return new FindUsernameResponseDto(user.getUsername());
    }

    @Transactional
    public MyInfoResponseDto myInfo(User user){

        List<StudyGroupUser> studyGroupUserList = studyGroupUserRepository.findAllByUserId(user.getId());

        List<JoinStudyGroupRequest> joinStudyGroupRequestList =
                joinStudyGroupRepository.findAllByUserId(user.getId());

        List<MyStudyGroups> myStudyGroupList = studyGroupUserList.stream()
                .map(studyGroupUser -> new MyStudyGroups(
                        studyGroupUser.getStudyGroup().getId(),studyGroupUser.getStudyGroup().getGroupName(),
                        studyGroupUser.getStudyGroupRole())).collect(Collectors.toList());

        List<MyJoinRequest> myJoinRequestList = joinStudyGroupRequestList.stream()
                .map(request -> new MyJoinRequest(request.getStudyGroup().getId(),
                        request.getStudyGroup().getGroupName(),request.getStatus()))
                        .collect(Collectors.toList());

        return MyInfoResponseDto.builder()
                .nickName(user.getNickname())
                .email(user.getEmail())
                .studyGroups(myStudyGroupList)
                .joinRequests(myJoinRequestList)
                .build();
    }
}
