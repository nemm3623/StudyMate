package com.example.studymate.repository;

import com.example.studymate.domain.StudyGroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupUserRepository extends JpaRepository<StudyGroupUser, Long> {


}
