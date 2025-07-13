package com.example.studymate.dto.StudyGroup;

import com.example.studymate.domain.JoinStudyGroupRequest;
import com.example.studymate.domain.JoinStudyGroupStatus;
import lombok.Getter;

@Getter
public class JoinStudyGroupRequestDTO {

    private Long id;
    private Long studyGroupId;
    private Long userId;
    private String nickName;
    private JoinStudyGroupStatus status;

    public JoinStudyGroupRequestDTO(JoinStudyGroupRequest request) {
        this.id = request.getId();
        this.studyGroupId = request.getStudyGroup().getId();
        this.userId = request.getUser().getId();
        this.nickName = request.getUser().getNickname();
        this.status = request.getStatus();
    }
}
