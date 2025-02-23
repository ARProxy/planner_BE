package com.zipple.module.member;

import com.zipple.common.auth.jwt.token.AuthTokens;
import com.zipple.common.auth.jwt.token.AuthTokensGenerator;
import com.zipple.common.oauth.OAuthInfoResponse;
import com.zipple.common.oauth.OAuthLoginParams;
import com.zipple.common.oauth.RequestOAuthInfoService;
import com.zipple.common.utils.GetMember;
import com.zipple.module.member.common.entity.User;
import com.zipple.module.member.common.repository.UserRepository;
import com.zipple.module.member.oauth.model.AuthLoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OAuthLoginService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final long REFRESH_TOKEN_EXPIRATION = 5183999L;
    private final UserRepository userRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final RestTemplate restTemplate;
    private final GetMember getMember;

    public AuthLoginResponse login(OAuthLoginParams params) {
        Map<String, Object> response = requestOAuthInfoService.request(params);
        OAuthInfoResponse oAuthInfoResponse = (OAuthInfoResponse) response.get("o_auth_info");
        String kakaoAccessToken = response.get("access_token").toString();
        String refreshToken = response.get("refresh_token").toString();

        Long userId = findOrCreateMember(oAuthInfoResponse);
        AuthTokens authTokens = authTokensGenerator.generate(userId);
        User user = userRepository.findById(userId).orElse(null);

        saveRedisRefreshToken(userId, kakaoAccessToken);

        assert user != null;
        if(user.getAgentUser() == null && user.getGeneralUser() == null) {
            return new AuthLoginResponse(authTokens.getAccessToken(), false);
        } else {
            return new AuthLoginResponse(authTokens.getAccessToken(), true);
        }
    }

    protected Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return userRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(User::getId)
                .orElseGet(() -> newMember(oAuthInfoResponse));
    }

    protected Long newMember(OAuthInfoResponse oAuthInfoResponse) {
        User user = User.builder()
                .email(oAuthInfoResponse.getEmail())
                .nickname(oAuthInfoResponse.getNickname())
                .gender(oAuthInfoResponse.getGender())
                .age_range(oAuthInfoResponse.getAge_range())
                .profile_image_url(oAuthInfoResponse.getProfile_image_url())
                .birthday(oAuthInfoResponse.getBirthday())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .build();
        return userRepository.save(user).getId();
    }

    private void saveRedisRefreshToken(Long userId, String refreshToken) {
        String key = "user:" + userId + ":refreshToken";
        redisTemplate.opsForValue().set(key, refreshToken, REFRESH_TOKEN_EXPIRATION, TimeUnit.SECONDS);
    }

    private String getRefreshToken(Long userId) {
        return redisTemplate.opsForValue().get(getRefreshTokenkey(userId));
    }

    private String getRefreshTokenkey(Long userId) {
        return "user:" + userId + ":refreshToken";
    }

    private void deleteRedisRefreshToken(Long userId) {
        redisTemplate.delete(getRefreshTokenkey(userId));
    }

    private Long userId() {
        User user = getMember.getCurrentMember();
        return user.getId();
    }

    public boolean logout() {
        String url = "https://kapi.kakao.com/v1/user/logout";


        String refreshToken = getRefreshToken(userId());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + refreshToken);

        HttpEntity<?> request = new HttpEntity<>("", httpHeaders);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, String.class
            );

            log.info("카카오 로그아웃 응답: {}", response.getBody());
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException e) {
            log.error("카카오 로그아웃 실패: {}", e.getMessage());
            return false;
        }
    }

    public boolean withdraw() {
        String url = "https://kapi.kakao.com/v1/user/unlink";

        String refreshToken = getRefreshToken(userId());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + refreshToken);

        HttpEntity<?> request = new HttpEntity<>("", httpHeaders);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, String.class
            );
            log.info("카카오 회원탈퇴 응답: {}", response.getBody());
            boolean result = response.getStatusCode().is2xxSuccessful();
            deleteRedisRefreshToken(userId());
            return result;
        } catch (HttpClientErrorException e) {
            log.error("카카오 회원탈퇴 실패: {}", e.getMessage());
            return false;
        }

    }
}
