package com.example.studymate.dto.StudyGroup;

import com.example.studymate.domain.StudyGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class StudyGroupDto {

    private String groupName;
    private String leaderUserName;
    private String subject;
    private int numberOfStudents;

    public StudyGroupDto(StudyGroup group) {
        this.groupName = group.getGroupName();
        this.subject = group.getSubject();
        this.leaderUserName = group.getLeader().getUsername();
        this.numberOfStudents = group.getNumberOfUser();
    }

}
