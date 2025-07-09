package com.example.studymate.repository;

import com.example.studymate.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User>findByNicknameAndEmail(String nickname, String email);

    @Modifying
    @Query("update User u set u.password = :password where u.username = :username")
    void updateByPassword(@Param("username") String username, @Param("password") String password);


}
