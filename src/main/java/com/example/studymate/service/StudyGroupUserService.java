package com.example.studymate.service;

import com.example.studymate.domain.StudyGroup;
import com.example.studymate.domain.StudyGroupRole;
import com.example.studymate.domain.StudyGroupUser;
import com.example.studymate.domain.User;
import com.example.studymate.dto.StudyGroup.JoinGroupRequestDto;
import com.example.studymate.dto.StudyGroup.LeaveGroupRequestDto;
import com.example.studymate.dto.StudyGroup.RemoveMemberRequestDto;
import com.example.studymate.dto.StudyGroup.TransferLeaderDto;
import com.example.studymate.exception.ErrorCode;
import com.example.studymate.exception.UserNotFoundException;
import com.example.studymate.repository.StudyGroupRepository;
import com.example.studymate.repository.StudyGroupUserRepository;
import com.example.studymate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyGroupUserService {

    private final UserRepository userRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final StudyGroupUserRepository studyGroupUserRepository;

    private final UserContextService userContextService;


    // 그룹 탈퇴
    @Transactional
    public void leaveStudyGroup(LeaveGroupRequestDto dto) {

        User user = userContextService.getCurrentUser();

        StudyGroup group = userContextService.getCurrentStudyGroup(dto.getGroupName());

        StudyGroupUser studyGroupUser = userContextService.getCurrentStudyGroupUser(group, user.getId());

        if(studyGroupUser.getStudyGroupRole().equals(StudyGroupRole.LEADER))
            throw new IllegalArgumentException("리더는 그룹을 떠날 수 없습니다.");

        studyGroupUserRepository.delete(studyGroupUser);
        group.decreaseNumberOfUser();
    }


    // 그룹원 추방 (리더 권한)
    @Transactional
    public void removeMemberFromGroup(RemoveMemberRequestDto dto){
        User user = userContextService.getCurrentUser();

        User targetUser = userRepository.findByUsername(dto.getUserName())
                .orElseThrow(()->new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        StudyGroup group = userContextService.getCurrentStudyGroup(dto.getGroupName());

        StudyGroupUser studyGroupUser = userContextService.getCurrentStudyGroupUser(group,user.getId());

        StudyGroupUser targetStudyGroupUser = userContextService.getCurrentStudyGroupUser(group,targetUser.getId());

        if(!userContextService.isLeader(studyGroupUser.getStudyGroupRole())) { // 제거를 하려는 유저가 리더인지 확인
            throw new IllegalArgumentException("리더만 가능한 권한입니다.");
        }
        if(userContextService.isLeader(targetStudyGroupUser.getStudyGroupRole())) // 제거할 유저가 리더인지 확인
            throw new IllegalArgumentException("리더는 추방할 수 없습니다.");

        studyGroupUserRepository.delete(targetStudyGroupUser);
        group.decreaseNumberOfUser();
    }


    @Transactional
    public void transferLeader(Long groupId, TransferLeaderDto dto){
        User user = userContextService.getCurrentUser();

        StudyGroup studyGroup = studyGroupRepository.findById(groupId)
                .orElseThrow(()->new RuntimeException("존재하지 않는 그룹입니다."));

        StudyGroupUser studyGroupUser = userContextService.getCurrentStudyGroupUser(studyGroup, user.getId());

        if(!userContextService.isLeader(studyGroupUser.getStudyGroupRole()))
            throw new RuntimeException("리더만 가능한 권한입니다.");

        if(!studyGroupUserRepository.existsByStudyGroupIdAndUserId(groupId, dto.getUserId()))
            throw new RuntimeException("그룹에 존재하지 않는 유저입니다.");

        User targetUser = userRepository.findById(dto.getUserId())
                .orElseThrow(()-> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        StudyGroupUser studyGroupTargetUser = userContextService
                .getCurrentStudyGroupUser(studyGroup, targetUser.getId());

        studyGroupUser.changeStudyGroupRole(StudyGroupRole.MEMBER);
        studyGroupTargetUser.changeStudyGroupRole(StudyGroupRole.LEADER);

        studyGroup.transferLeader(targetUser);

    }

}
