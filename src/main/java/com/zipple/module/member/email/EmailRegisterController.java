package com.zipple.module.member.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zipple.common.auth.jwt.token.AuthTokens;
import com.zipple.module.member.email.model.EmailAgentRequest;
import com.zipple.module.member.email.model.EmailGeneralRequest;
import com.zipple.module.member.email.model.EmailLoginRequest;
import com.zipple.module.member.email.model.EmailLoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "이메일 회원가입/로그인")
@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/email")
@RequiredArgsConstructor
public class EmailRegisterController {

    private final EmailRegisterService emailRegisterService;
    private final EmailLoginService emailLoginService;

    @Operation(
            summary = "이메일 일반 회원 가입"
            , description = "이메일 회원 가입이기 때문에 JWT 없이 요청"
    )
    @PostMapping(
            value = "/general",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> emailRegisterGeneral(
            @Parameter(
                    name = "EmailGeneralRequest",
                    description = "이메일 회원 일반 가입 요청 데이터"
            )
            @RequestBody EmailGeneralRequest emailGeneralRequest
            ) {
        emailRegisterService.emailRegisterGeneral(emailGeneralRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "이메일 공인중개사 회원 가입"
            , description = "이메일 회원 가입이기 때문에 JWT 없이 요청"
    )
    @PostMapping(
            value = "/agent",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> emailRegisterAgent(
            @Parameter(
                    name = "realEstateAgencyCertificate",
                    description = "중개 사무소 등록증"
            )
            @RequestPart(value = "realEstateAgencyCertificate") MultipartFile realEstateAgencyCertificate,
            @Parameter(
                    name = "businessCertificate",
                    description = "사업자 등록증"
            )
            @RequestPart(value = "businessCertificate") MultipartFile businessCertificate,
            @Parameter(
                    name = "realEstateAgentLicense",
                    description = "공인중개사 자격증"
            )
            @RequestPart(value = "realEstateAgentLicense") MultipartFile realEstateAgentLicense,
            @Parameter(
                    name = "agentImage",
                    description = "공인 중개사 본인 인증 사진"
            )
            @RequestPart(value = "agentImage") MultipartFile agentImage,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "이메일 회원가입 요청 데이터(JSON)",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmailAgentRequest.class)
                    )
            )
            @RequestPart(value = "emailAgentRequest") String emailAgentRequest
            ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        EmailAgentRequest emailAgentRequests = objectMapper.readValue(emailAgentRequest, EmailAgentRequest.class);

        List<MultipartFile> agentCertificationDocuments = List.of(realEstateAgencyCertificate, businessCertificate, realEstateAgentLicense);
        emailRegisterService.emailRegisterAgent(emailAgentRequests, agentCertificationDocuments, agentImage);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "이메일 로그인"
            , description = "로그인 성공 후 JWT 응답"
    )
    @PostMapping(
            value = "/login"
    )
    public ResponseEntity<EmailLoginResponse> emailRegisterLogin(
            @RequestBody EmailLoginRequest emailLoginRequest
    ) {
        EmailLoginResponse authTokens = emailLoginService.emailLogin(emailLoginRequest);
        return ResponseEntity.ok(authTokens);
    }
}
