package com.zipple.module.mypage.agent.portfolio.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "중개사 상세 프로필 포트폴리오 데이터")
public class PortfolioProfile {
    private String profileImage;
    private String title;
    private String createdAt;
}
