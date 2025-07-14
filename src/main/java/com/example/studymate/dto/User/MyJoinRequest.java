package com.example.studymate.dto.User;

import com.example.studymate.domain.JoinStudyGroupStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyJoinRequest {
    private Long groupId;
    private String groupName;
    private JoinStudyGroupStatus joinStudyGroupStatus;
}
