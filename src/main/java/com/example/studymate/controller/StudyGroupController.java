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


//    @PostMapping("/join")
//    public ResponseEntity<String> joinStudyGroup(@RequestBody JoinGroupRequestDto dto) {
//        studyGroupService.joinStudyGroupRequest(dto);
//        return ResponseEntity.ok(dto.getGroupName() + " 스터디 그룹에 가입하였습니다.");
//    }


    @PostMapping("/{groupId}/delete")
    public ResponseEntity<String> deleteStudyGroup(@PathVariable Long groupId) {
        studyGroupService.deleteStudyGroup(groupId);
        return ResponseEntity.ok(" 스터디 그룹이 삭제되었습니다.");
    }


    @PostMapping("/{groupId}/join")
    public ResponseEntity<String> joinStudyGroup(@PathVariable Long groupId, @RequestBody JoinGroupRequestDto dto) {
        studyGroupService.joinStudyGroupRequest(groupId, dto);
        return ResponseEntity.ok("가입 신청이 완료되었습니다.");
    }


    @GetMapping("/{groupId}/leader/join/list")
    public ResponseEntity<Page<JoinStudyGroupRequestDTO>> joinStudyGroupList(@PathVariable Long groupId,Pageable pageable) {
        Page<JoinStudyGroupRequestDTO> requests = studyGroupService.getAllJoinStudyGroupRequests(pageable, groupId);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/{groupId}/leader/join/{requestId}/approve")
    public ResponseEntity<String> approveStudyGroup(@PathVariable Long groupId, @PathVariable Long requestId) {
        studyGroupService.approveJoinStudyGroupRequest(groupId, requestId);
        return ResponseEntity.ok("해당 가입요청을 승인하였습니다.");
    }

    @PostMapping("/{groupId}/leader/join/{requestId}/reject")
    public ResponseEntity<String> rejectStudyGroup(@PathVariable Long groupId, @PathVariable Long requestId) {
        studyGroupService.rejectJoinStudyGroupRequest(groupId, requestId);
        return ResponseEntity.ok("해당 가입요청을 거절하였습니다.");
    }
}
