package com.zipple.common.oauth;

public interface OAuthInfoResponse {

    String getEmail();
    String getNickname();
    String getGender();
    String getAge_range();
    String getProfile_image_url();
    String getBirthday();
    OAuthProvider getOAuthProvider();
}
