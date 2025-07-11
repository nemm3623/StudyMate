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
public class StudyGroupUser {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "StudyGroup_id", nullable = false)
    private StudyGroup studyGroup;

    @ManyToOne
    @JoinColumn(name = "User_id", nullable = false)
    private User user;

}
