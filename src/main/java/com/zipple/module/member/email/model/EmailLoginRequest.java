package com.zipple.module.member.email.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "emailLoginRequest", description = "email 로그인 데이터")
public class EmailLoginRequest {

    @Schema(description = "이메일", example = "kakao@kakao.com")
    private String email;

    @Schema(description = "이메일", example = "kakao@kakao.com")
    private String password;
}
