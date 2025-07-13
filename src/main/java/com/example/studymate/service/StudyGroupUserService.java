package com.example.studymate.service;

import com.example.studymate.domain.StudyGroup;
import com.example.studymate.domain.StudyGroupUser;
import com.example.studymate.domain.User;
import com.example.studymate.dto.StudyGroup.RemoveMemberRequestDto;
import com.example.studymate.exception.ErrorCode;
import com.example.studymate.exception.UserNotFoundException;
import com.example.studymate.repository.StudyGroupUserRepository;
import com.example.studymate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyGroupUserService {

    private final StudyGroupUserRepository studyGroupUserRepository;
    private final UserRepository userRepository;

    private final StudyGroupService studyGroupService;


    // 그룹원 추방
    @Transactional
    public void removeMemberFromGroup(RemoveMemberRequestDto dto){
        User user = studyGroupService.getCurrentUser();

        User targetUser = userRepository.findByUsername(dto.getUserName())
                .orElseThrow(()->new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        StudyGroup group = studyGroupService.getCurrentStudyGroup(dto.getGroupName());

        StudyGroupUser studyGroupUser = studyGroupService
                .getCurrentStudyGroupUser(group,user.getId());

        StudyGroupUser targetStudyGroupUser = studyGroupService
                .getCurrentStudyGroupUser(group,targetUser.getId());

        if(!studyGroupService.isLeader(studyGroupUser.getStudyGroupRole())) { // 제거를 하려는 유저가 리더인지 확인
            throw new IllegalArgumentException("리더만 가능한 권한입니다.");
        }
        if(studyGroupService.isLeader(targetStudyGroupUser.getStudyGroupRole())) // 제거할 유저가 리더인지 확인
            throw new IllegalArgumentException("리더는 추방할 수 없습니다.");

        studyGroupUserRepository.delete(targetStudyGroupUser);
        group.decreaseNumberOfUser();
    }
}
