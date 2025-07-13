package com.example.studymate.service;

import com.example.studymate.domain.*;
import com.example.studymate.dto.StudyGroup.*;
import com.example.studymate.exception.ErrorCode;
import com.example.studymate.exception.UserNotFoundException;
import com.example.studymate.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class StudyGroupService {

    private final StudyGroupRepository studygroupRepository;
    private final StudyGroupUserRepository studygroupUserRepository;
    private final JoinStudyGroupRepository joinStudygroupRepository;
    private final UserContextService userContextService;


    @Transactional(readOnly = true)
    public Page<StudyGroupDto> getAllStudyGroups(Pageable pageable) {
        // Page<> = 데이터를 페이지 단위로 반환해주는 객체
        // Pageable = 페이지 번호나 정렬기준 등을 담고 있는 객체

        return studygroupRepository.findAll(pageable).map(StudyGroupDto::new);
    }

    // 그룹 생성
    @Transactional
    public void createStudyGroup(CreateGroupRequestDto dto) {

        // 그룹을 만든 유저 객체 가져오기
        User user = userContextService.getCurrentUser();

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

        group.increaseNumberOfUser();

        studygroupRepository.save(group);
        studygroupUserRepository.save(groupUser);

    }


    // 그룹 삭제
    @Transactional
    public void deleteStudyGroup(DeleteGroupRequestDto dto){

        User user = userContextService.getCurrentUser();

        StudyGroup group = studygroupRepository.findByGroupName(dto.getGroupName())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 그룹입니다."));

        StudyGroupUser studyGroupUser = userContextService.getCurrentStudyGroupUser(group,user.getId());

        if(!userContextService.isLeader(studyGroupUser.getStudyGroupRole()))
            throw new IllegalArgumentException("리더만 가능한 권한입니다.");

        studygroupUserRepository.deleteAllByStudyGroup(group);
        studygroupRepository.delete(group);

    }

    //그룹 가입 요청
    @Transactional
    public void joinStudyGroupRequest(JoinGroupRequestDto dto) {

        User user = userContextService.getCurrentUser();
        StudyGroup studyGroup = userContextService.getCurrentStudyGroup(dto.getGroupName());

        JoinStudyGroupRequest request = JoinStudyGroupRequest.builder()
                .user(user)
                .studyGroup(studyGroup)
                .status(JoinStudyGroupStatus.PENDING)
                .build();

        joinStudygroupRepository.save(request);
    }
}
