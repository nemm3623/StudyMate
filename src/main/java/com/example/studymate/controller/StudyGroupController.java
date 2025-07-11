package com.example.studymate.controller;

import com.example.studymate.domain.StudyGroup;
import com.example.studymate.dto.StudyGroup.CreateGroupRequestDto;
import com.example.studymate.dto.StudyGroup.JoinGroupRequestDto;
import com.example.studymate.service.StudyGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/study-group")
@RequiredArgsConstructor
public class StudyGroupController {

    private final StudyGroupService studyGroupService;

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

}
