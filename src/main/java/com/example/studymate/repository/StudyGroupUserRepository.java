package com.example.studymate.repository;

import com.example.studymate.domain.StudyGroup;
import com.example.studymate.domain.StudyGroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyGroupUserRepository extends JpaRepository<StudyGroupUser, Long> {

    Optional<StudyGroupUser> findByStudyGroupAndUserId(StudyGroup studyGroup, Long userId);
    void deleteAllByStudyGroup(StudyGroup studyGroup);

}
