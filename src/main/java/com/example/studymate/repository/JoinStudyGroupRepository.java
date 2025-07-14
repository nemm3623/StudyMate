package com.example.studymate.repository;

import com.example.studymate.domain.JoinStudyGroupRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JoinStudyGroupRepository extends JpaRepository<JoinStudyGroupRequest,Long> {

    boolean existsByStudyGroupIdAndUserId(Long studyGroup_id, Long user_id);
    List<JoinStudyGroupRequest> findAllByUserId(Long userId);

}
