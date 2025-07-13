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

    @Column(nullable = false)
    private String introduction;

    @Enumerated(EnumType.STRING)
    private JoinStudyGroupStatus status;



    public void approve() {
        if (this.status != JoinStudyGroupStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 요청입니다.");
        }
        this.status = JoinStudyGroupStatus.APPROVED;
    }

    public void reject() {
        if (this.status != JoinStudyGroupStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 요청입니다.");
        }
        this.status = JoinStudyGroupStatus.REJECTED;
    }

}
