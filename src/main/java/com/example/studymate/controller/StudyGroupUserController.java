package com.example.studymate.controller;

import com.example.studymate.dto.StudyGroup.LeaveGroupRequestDto;
import com.example.studymate.dto.StudyGroup.RemoveMemberRequestDto;
import com.example.studymate.service.StudyGroupUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudyGroupUserController {

    private final StudyGroupUserService studyGroupUserService;


    @PostMapping("/leave")
    public ResponseEntity<String> leaveStudyGroup(@RequestBody LeaveGroupRequestDto dto) {
        studyGroupUserService.leaveStudyGroup(dto);
        return ResponseEntity.ok(dto.getGroupName() + " 스터디 그룹을 탈퇴하였습니다.");
    }


    @PostMapping("/remove")
    public ResponseEntity<String> removeMember(@RequestBody RemoveMemberRequestDto dto) {
        studyGroupUserService.removeMemberFromGroup(dto);
        return ResponseEntity.ok(dto.getUserName() + "을(를) "
                + dto.getGroupName() + " 스터디 그룹에서 추방하였습니다.");
    }

}
