package com.zipple.module.member.oauth.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "OAuth 로그인 후 추가 일반 사용자 요청 데이터")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralUserRequest {

    @Schema(description = "사용자 이름", example = "권동휘")
    private String generalName;

    @Schema(description = "일반 사용자 번호", example = "010-2211-2312")
    private String generalNumber;

    @Schema(description = "주소", example = "서울특별시 역삼3동")
    private String generalAddress;

    @Schema(description = "주거 형태", example = "아파트")
    private String housingType;

    @Schema(description = "마케팅 수신 동의", example = "false")
    private Boolean marketingNotificationTerms;
}
