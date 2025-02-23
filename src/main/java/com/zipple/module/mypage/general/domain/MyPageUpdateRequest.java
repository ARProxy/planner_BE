package com.zipple.module.mypage.general.domain;

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
public class MyPageUpdateRequest {
    @Schema(description = "사용자 이름", example = "이지혜")
    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String generalName;

    @Schema(description = "이메일", example = "kakao@kakao.com")
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "유효한 이메일 주소 형식이 아닙니다.")
    private String email;

    @Schema(description = "폰 번호", example = "010-2222-1111")
    private String phoneNumber;

    @Schema(description = "주소", example = "서울특별시 역삼3동")
    @NotBlank(message = "주소는 필수 입력 항목입니다.")
    private String generalAddress;

    @Schema(description = "주거 형태", example = "문자열로 보내주면 enum 처리할 거임")
    private String housingType;
}
