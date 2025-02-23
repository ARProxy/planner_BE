package com.zipple.common.oauth;

import com.zipple.common.oauth.kakao.KakaoTokenResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RequestOAuthInfoService {

    private final Map<OAuthProvider, OAuthApiClient> clients;

    public RequestOAuthInfoService(List<OAuthApiClient> clients) {
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(OAuthApiClient::oAuthProvider, Function.identity())
        );
    }

    public Map<String, Object> request(OAuthLoginParams params) {
        Map<String, Object> response = new HashMap<>();
        OAuthApiClient client = clients.get(params.oAuthProvider());
        KakaoTokenResponse accessToken = client.requestAccessToken(params);
        response.put("access_token", accessToken.getAccessToken());
        response.put("refresh_token", accessToken.getRefreshToken());
        response.put("o_auth_info", client.requestOauthInfo(accessToken.getAccessToken()));
        return response;
    }
}
