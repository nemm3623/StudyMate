package com.example.studymate.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyGroup {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String groupName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_id", nullable = false)
    private User leader;

    @Column
    private int numberOfUser = 0;

    public void increaseNumberOfUser() {
        numberOfUser++;
    }
    public void decreaseNumberOfUser() {
        numberOfUser--;
    }

    public void transferLeader(User leader){
        if(Objects.equals(this.leader.getId(), leader.getId()))
            throw new IllegalArgumentException("이미 그룹의 리더입니다. 다른 유저를 선택하세요.");
        this.leader = leader;
    }
}
