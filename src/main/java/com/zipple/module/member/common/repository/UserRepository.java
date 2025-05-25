package com.zipple.module.member.common.repository;

import com.zipple.module.member.common.entity.User;
import com.zipple.module.member.common.entity.category.AgentSpecialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.agentUser au WHERE au.agentSpecialty = :category")
    Page<User> findByAgentUser_AgentSpecialty(@Param("category") AgentSpecialty category, Pageable pageable);

    Optional<User> findByMainEmail(String email);
}
