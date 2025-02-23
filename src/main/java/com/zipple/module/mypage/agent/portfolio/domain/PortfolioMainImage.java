package com.zipple.module.mypage.agent.portfolio.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortfolioMainImage {
    @Schema(description = "포트폴리오 ID", example = "1")
    private Long portfolioId;

    @Schema(description = "포트폴리오 제목", example = "Modern Architecture")
    private String portfolioTitle;

    @Schema(description = "대표 이미지 URL", example = "http://localhost:8081/images/architecture.jpg")
    private String mainImageUrl;

    @Schema(description = "포트폴리오 생성 날짜", example = "2025-01-19T12:00:00")
    private LocalDateTime createdAt;
}
