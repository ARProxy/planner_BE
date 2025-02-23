package com.zipple.common.auth.jwt.token;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "액세스 토큰")
public class AuthTokens {
    private String accessToken;

    public static AuthTokens of(String accessToken) {
        return new AuthTokens(accessToken);
    }
}
