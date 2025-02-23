package com.zipple.module.mypage.agent.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageAgentResponse {

    @Schema(description = "중개사 이름", example = "이지혜")
    private String agentName;

    @Schema(description = "대표자 이름", example = "권동휘")
    private String ownerName;

    @Schema(description = "중개사 구분", example = "개업")
    private String agentType;

    @Schema(description = "폰 번호", example = "010-2222-1111")
    private String phoneNumber;

    @Schema(description = "이메일", example = "agent@example.com")
    private String email;

    @Schema(description = "생성일", example = "2025-01-28T00:53:11.396059")
    private String createAt;
}
