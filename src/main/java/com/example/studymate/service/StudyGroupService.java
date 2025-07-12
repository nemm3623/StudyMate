package com.example.studymate.service;

import com.example.studymate.domain.Role;
import com.example.studymate.domain.StudyGroup;
import com.example.studymate.domain.StudyGroupUser;
import com.example.studymate.domain.User;
import com.example.studymate.dto.StudyGroup.CreateGroupRequestDto;
import com.example.studymate.dto.StudyGroup.JoinGroupRequestDto;
import com.example.studymate.dto.StudyGroup.StudyGroupDto;
import com.example.studymate.exception.ErrorCode;
import com.example.studymate.exception.UserNotFoundException;
import com.example.studymate.repository.RoleRepository;
import com.example.studymate.repository.StudyGroupRepository;
import com.example.studymate.repository.StudyGroupUserRepository;
import com.example.studymate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class StudyGroupService {

    private final UserRepository userRepository;
    private final StudyGroupRepository studygroupRepository;
    private final StudyGroupUserRepository studygroupUserRepository;
    private final RoleRepository roleRepository;


    @Transactional(readOnly = true)
    public Page<StudyGroupDto> getAllStudyGroups(Pageable pageable) {
        // Page<> = 데이터를 페이지 단위로 반환해주는 객체
        // Pageable = 페이지 번호나 정렬기준 등을 담고 있는 객체

        return studygroupRepository.findAll(pageable).map(StudyGroupDto::new);
    }

    @Transactional
    public void createStudyGroup(CreateGroupRequestDto dto) {

        // 그룹을 만든 유저 객체 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        Role role = roleRepository.findByName("LEADER");

        StudyGroup group = StudyGroup.builder()
                .groupName(dto.getGroupName())
                .description(dto.getDescription())
                .subject(dto.getSubject())
                .leader(user)
                .build();

        StudyGroupUser groupUser = StudyGroupUser.builder()
                .user(user)
                .studyGroup(group)
                .build();

        studygroupRepository.save(group);
        studygroupUserRepository.save(groupUser);

        user.addRole(role);

        group.increaseNumberOfUser();

    }

    @Transactional
    public void joinStudyGroup(JoinGroupRequestDto dto) {

        StudyGroup group = studygroupRepository.findByGroupName(dto.getGroupName()).
                orElseThrow(()-> new IllegalArgumentException("존재하지 않는 그룹입니다."));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        StudyGroupUser groupUser = StudyGroupUser.builder()
                .user(user)
                .studyGroup(group)
                .build();

        studygroupUserRepository.save(groupUser);
        group.increaseNumberOfUser();

    }





}
