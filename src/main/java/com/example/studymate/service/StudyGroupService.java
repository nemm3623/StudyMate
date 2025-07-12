package com.example.studymate.service;

import com.example.studymate.domain.*;
import com.example.studymate.dto.StudyGroup.*;
import com.example.studymate.exception.AuthFailedException;
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
        User user = getCurrentUser();

        StudyGroup group = StudyGroup.builder()
                .groupName(dto.getGroupName())
                .description(dto.getDescription())
                .subject(dto.getSubject())
                .leader(user)
                .build();

        StudyGroupUser groupUser = StudyGroupUser.builder()
                .user(user)
                .studyGroup(group)
                .studyGroupRole(StudyGroupRole.LEADER)
                .build();

        studygroupRepository.save(group);
        studygroupUserRepository.save(groupUser);

        group.increaseNumberOfUser();

    }

    @Transactional
    public void joinStudyGroup(JoinGroupRequestDto dto) {

        StudyGroup group = studygroupRepository.findByGroupName(dto.getGroupName()).
                orElseThrow(()-> new IllegalArgumentException("존재하지 않는 그룹입니다."));

        User user = getCurrentUser();

        StudyGroupUser groupUser = StudyGroupUser.builder()
                .user(user)
                .studyGroup(group)
                .studyGroupRole(StudyGroupRole.MEMBER)
                .build();

        studygroupUserRepository.save(groupUser);
        group.increaseNumberOfUser();

    }

    @Transactional
    public void leaveStudyGroup(LeaveGroupRequestDto dto) {

        User user = getCurrentUser();

        StudyGroup group = studygroupRepository.findByGroupName(dto.getGroupName())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 그룹입니다."));

        StudyGroupUser studyGroupUser = studygroupUserRepository.findByStudyGroupAndUserId(group,user.getId())
                .orElseThrow(()->new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        if(studyGroupUser.getStudyGroupRole().equals(StudyGroupRole.LEADER))
            throw new IllegalArgumentException("리더는 그룹을 떠날 수 없습니다.");

        studygroupUserRepository.delete(studyGroupUser);
        group.decreaseNumberOfUser();
    }

    @Transactional
    public void deleteStudyGroup(DeleteGroupRequestDto dto){

        User user = getCurrentUser();

        StudyGroup group = studygroupRepository.findByGroupName(dto.getGroupName())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 그룹입니다."));

        StudyGroupUser studyGroupUser = studygroupUserRepository
                .findByStudyGroupAndUserId(group, user.getId())
                .orElseThrow(()->new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        checkLeader(studyGroupUser.getStudyGroupRole());

        studygroupUserRepository.deleteAllByStudyGroup(group);
        studygroupRepository.delete(group);

    }

    public void checkLeader(StudyGroupRole role){
        if(role != StudyGroupRole.LEADER)
            throw new IllegalArgumentException("리더만 가능한 권한입니다.");
    }

    public User getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

}
