package com.zipple.module.member;

import com.zipple.common.oauth.kakao.KakaoLoginParams;
import com.zipple.module.member.oauth.model.AuthLoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "소셜 로그인")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin
public class OAuthController {
    private final OAuthLoginService oAuthLoginService;

    @Operation(
            summary = "카카오 로그인"
            ,description = "authorizationCode: url에 code값 입력"
    )
    @PostMapping("/kakao")
    public ResponseEntity<AuthLoginResponse> loginKakao(@RequestBody KakaoLoginParams params) {
        log.info("kakao code: {}", params);
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }

    @Operation(summary = "카카오 로그아웃")
    @PatchMapping(value = "/logout")
    public ResponseEntity<String> logoutKakao() {
        try {
            boolean result = oAuthLoginService.logout();
            if (result) {
                return ResponseEntity.ok("로그아웃 성공");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그아웃 실패");
            }
        } catch (Exception e) {
            log.error("카카오 로그아웃 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그아웃 처리 중 오류 발생");
        }
    }

    @Operation(summary = "카카오 회원탈퇴")
    @DeleteMapping(value = "/withdraw")
    public ResponseEntity<String> withdraw() {
        oAuthLoginService.withdraw();
        return ResponseEntity.ok("회원탈퇴 성공");
    }
}
