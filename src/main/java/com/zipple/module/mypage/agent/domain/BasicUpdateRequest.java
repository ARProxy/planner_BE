package com.zipple.module.mypage.agent.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasicUpdateRequest {
    @Schema(description = "이메일", example = "kakao@kakao.com")
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "유효한 이메일 주소 형식이 아닙니다.")
    private String email;

    @Schema(description = "공인중개사 구분", example = "소속 또는 개업 (BUSINESS_AGENT, AFFILIATED_AGENT)")
    private String agentType;

    @Schema(description = "폰 번호", example = "010-2222-1111")
    private String phoneNumber;

    @Schema(description = "외부 링크", example = "https://blog.naver.com/yourlink")
    private String externalLink;

    @Schema(description = "자기 소개글 제목", example = "새로운 소개 제목")
    private String title;

    @Schema(description = "자기 소개글 내용", example = "자기 소개글 내용을 여기에 작성하세요.")
    private String content;
}
