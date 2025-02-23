package com.zipple.module.mypage.agent.portfolio;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    int countByUserId(long userId);

    List<Portfolio> findTop6ByUserIdOrderByCreatedAtDesc(Long userId);
}
