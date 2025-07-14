package com.example.studymate.dto.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MyInfoResponseDto {

    private String nickName;
    private String email;

    private List<MyStudyGroups> studyGroups;

    private List<MyJoinRequest> joinRequests;

}
