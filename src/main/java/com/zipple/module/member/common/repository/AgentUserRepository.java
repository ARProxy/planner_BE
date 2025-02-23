package com.zipple.module.member.common.repository;

import com.zipple.module.member.common.entity.AgentUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentUserRepository extends JpaRepository<AgentUser, Long> {
}
