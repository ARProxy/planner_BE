package com.zipple.common.oauth.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zipple.common.oauth.OAuthInfoResponse;
import com.zipple.common.oauth.OAuthProvider;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse implements OAuthInfoResponse {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {
        private KakaoProfile profile;
        private String email;
        private String gender;
        private String age_range;
        private String birthday;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoProfile {
        private String nickname;
        private String profile_image_url;
    }

    @Override
    public String getEmail() {
        return kakaoAccount.getEmail();
    }

    @Override
    public String getNickname() {
        return kakaoAccount.profile.nickname;
    }

    @Override
    public String getGender() {
        return kakaoAccount.getGender();
    }

    @Override
    public String getProfile_image_url() {
        return kakaoAccount.profile.profile_image_url;
    }

    @Override
    public String getAge_range() {
        return kakaoAccount.getAge_range();
    }

    @Override
    public String getBirthday() {
        return kakaoAccount.getBirthday();
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }

}
