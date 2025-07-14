package com.example.studymate.dto.User;

import com.example.studymate.domain.StudyGroupRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyStudyGroups {
    private Long groupId;
    private String groupName;
    private StudyGroupRole studyGroupRole;
}
