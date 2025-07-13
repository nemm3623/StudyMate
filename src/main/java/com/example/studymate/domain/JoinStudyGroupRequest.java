package com.example.studymate.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinStudyGroupRequest {

    @Id @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private StudyGroup studyGroup;

    @Enumerated(EnumType.STRING)
    private JoinStudyGroupStatus status;

}
