package com.zipple.module.member.email.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "EmailGeneralRequest", description = "이메일 회원가입 요청 데이터")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailGeneralRequest {

    @Schema(description = "사용자 이름", example = "이지혜")
    private String generalName;

    @Schema(description = "일반 사용자 번호", example = "010-2211-2312")
    private String generalNumber;

    @Schema(description = "이메일", example = "kakao@kakao.com")
    private String email;

    @Schema(description = "비밀번호", example = "dong1234")
    private String password;

    @Schema(description = "주소", example = "서울특별시 역삼3동")
    private String generalAddress;

    @Schema(description = "주거 형태", example = "아파트")
    private String housingType;

    @Schema(description = "마케팅 수신 동의", example = "false")
    private Boolean marketingNotificationTerms;
}
