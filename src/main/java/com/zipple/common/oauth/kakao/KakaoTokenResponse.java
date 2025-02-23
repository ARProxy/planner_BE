package com.zipple.common.oauth.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoTokenResponse {
    private String accessToken;
    private String refreshToken;
}
