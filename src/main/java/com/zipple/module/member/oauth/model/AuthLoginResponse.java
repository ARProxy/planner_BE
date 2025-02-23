package com.zipple.module.member.oauth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthLoginResponse {
    @Schema(description = "액세스 토큰")
    private String accessToken;
    @Schema(description = "소셜 로그인 시 등록된 사용자인지 아닌지")
    private Boolean isRegistered;
}
