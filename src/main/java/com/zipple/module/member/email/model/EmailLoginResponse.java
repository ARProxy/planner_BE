package com.zipple.module.member.email.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailLoginResponse {
    @Schema(description = "액세스 토큰")
    private String accessToken;
    @Schema(description = "유저 타입 : GENERAL,\n" +
            "    BUSINESS_AGENT,\n" +
            "    AFFILIATED_AGENT,\n" +
            "    NOT_REGISTERED")
    private UserType userType;
}
