package com.zipple.common.oauth;

import com.zipple.common.oauth.kakao.KakaoTokenResponse;

public interface OAuthApiClient {

    OAuthProvider oAuthProvider();
    KakaoTokenResponse requestAccessToken(OAuthLoginParams params);
    OAuthInfoResponse requestOauthInfo(String accessToken);
}
