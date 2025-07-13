package com.example.studymate.service;

import com.example.studymate.domain.StudyGroup;
import com.example.studymate.domain.StudyGroupRole;
import com.example.studymate.domain.StudyGroupUser;
import com.example.studymate.domain.User;
import com.example.studymate.exception.ErrorCode;
import com.example.studymate.exception.UserNotFoundException;
import com.example.studymate.repository.StudyGroupRepository;
import com.example.studymate.repository.StudyGroupUserRepository;
import com.example.studymate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserContextService {

    private final UserRepository userRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final StudyGroupUserRepository studyGroupUserRepository;


    public boolean isLeader(StudyGroupRole role){
        return role == StudyGroupRole.LEADER;
    }


    public User getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
    }


    public StudyGroup getCurrentStudyGroup(String groupName){
        return studyGroupRepository.findByGroupName(groupName)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 그룹입니다."));
    }


    public StudyGroupUser getCurrentStudyGroupUser(StudyGroup group, long userId){
        return studyGroupUserRepository
                .findByStudyGroupAndUserId(group,userId)
                .orElseThrow(()->new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
