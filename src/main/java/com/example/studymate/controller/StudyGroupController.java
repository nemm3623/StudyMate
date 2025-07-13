package com.example.studymate.controller;

import com.example.studymate.domain.StudyGroup;
import com.example.studymate.dto.StudyGroup.*;
import com.example.studymate.service.StudyGroupService;
import com.example.studymate.service.StudyGroupUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/study-group")
@RequiredArgsConstructor
public class StudyGroupController {

    private final StudyGroupService studyGroupService;
    private final StudyGroupUserService studyGroupUserService;

    @GetMapping
        public ResponseEntity<Page<StudyGroupDto>> getAllStudyGroups(Pageable pageable) {
        Page<StudyGroupDto> studyGroups = studyGroupService.getAllStudyGroups(pageable);
        return ResponseEntity.ok(studyGroups);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createStudyGroup(@RequestBody CreateGroupRequestDto dto) {
        studyGroupService.createStudyGroup(dto);
        return ResponseEntity.ok(dto.getGroupName() + " 스터디 그룹이 성공적으로 만들어졌습니다.");
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinStudyGroup(@RequestBody JoinGroupRequestDto dto) {
        studyGroupService.joinStudyGroup(dto);
        return ResponseEntity.ok(dto.getGroupName() + " 스터디 그룹에 가입하였습니다.");
    }

    @PostMapping("/leave")
    public ResponseEntity<String> leaveStudyGroup(@RequestBody LeaveGroupRequestDto dto) {
        studyGroupService.leaveStudyGroup(dto);
        return ResponseEntity.ok(dto.getGroupName() + " 스터디 그룹을 탈퇴하였습니다.");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteStudyGroup(@RequestBody DeleteGroupRequestDto dto) {
        studyGroupService.deleteStudyGroup(dto);
        return ResponseEntity.ok(dto.getGroupName() + " 스터디 그룹이 삭제되었습니다.");
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeMember(@RequestBody RemoveMemberRequestDto dto) {
        studyGroupUserService.removeMemberFromGroup(dto);
        return ResponseEntity.ok(dto.getUserName() + "을(를) "
                + dto.getGroupName() + " 스터디 그룹에서 추방하였습니다.");
    }

}
