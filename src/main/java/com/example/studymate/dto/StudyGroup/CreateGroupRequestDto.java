package com.example.studymate.dto.StudyGroup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateGroupRequestDto {

    private String groupName;
    private String description;
    private String subject;

}
