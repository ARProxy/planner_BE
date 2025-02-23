package com.zipple.module.mainpage.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "중개사 매칭 데이터")
public class AgentMatchingResponse {

    @Schema(description = "유저 ID")
    private Long userId;
    @Schema(description = "프로필 이미지", example = "http://host:8081/zipple/...")
    private String profileUrl;
    @Schema(description = "거주 타입", example = "아파트")
    private String agentSpecialty;
    @Schema(description = "포트폴리오 개수", example = "20")
    private Integer portfolioCount;
    @Schema(description = "중개사 이름", example = "권동휘")
    private String agentName;
    @Schema(description = "자기소개 제목", example = "안녕하세요")
    private String title;
}
