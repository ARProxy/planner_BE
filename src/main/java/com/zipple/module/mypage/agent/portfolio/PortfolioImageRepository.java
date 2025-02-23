package com.zipple.module.mypage.agent.portfolio;

import com.zipple.module.member.common.entity.category.AgentType;
import com.zipple.module.mypage.agent.portfolio.domain.PortfolioMainImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PortfolioImageRepository extends JpaRepository<PortfolioImage, Long> {

    @Query("SELECT new com.zipple.module.mypage.agent.portfolio.domain.PortfolioMainImage(" +
            "pi.portfolio.id, p.title, pi.imageUrl, p.createdAt) " +
            "FROM PortfolioImage pi " +
            "JOIN pi.portfolio p " +
            "WHERE pi.isMain = true " +
            "AND p.agentType = :agentType " +
            "AND p.user.id = :userId " +
            "ORDER BY p.createdAt DESC")
    Page<PortfolioMainImage> findMainImagesWithPagination(@Param("userId") Long userId,
                                                          Pageable pageable,
                                                          @Param("agentType") AgentType agentType);

    @Query("SELECT pi FROM PortfolioImage pi " +
            "JOIN pi.portfolio p " +
            "WHERE p.user.id = :userId AND p.agentType = :agentType " +
            "AND pi.isMain = true")
    Page<PortfolioMainImage> findMainImagesByUserIdWithPagination(@Param("userId") Long userId,
                                                                  Pageable pageable,
                                                                  @Param("agentType") AgentType agentType);
}
