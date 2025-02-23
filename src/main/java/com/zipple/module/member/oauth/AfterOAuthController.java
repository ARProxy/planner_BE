package com.zipple.module.member.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zipple.module.member.oauth.model.AgentUserRequest;
import com.zipple.module.member.oauth.model.GeneralUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "소셜 로그인")
@RestController
@RequestMapping("/api/v1/register")
@RequiredArgsConstructor
public class AfterOAuthController {

    private final AfterOAuthService afterOAuthService;

    @Operation(
            summary = "일반 회원 가입 추가 정보",
            description = "일반 회원 가입"
    )
    @PostMapping(
            value = "/general",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> generalRegister(
            @RequestBody GeneralUserRequest generalUserRequest
            ) {
        afterOAuthService.generalRegister(generalUserRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "공인중개사 회원 가입 추가 정보",
            description = "공인 중개사 회원 가입"
    )
    @PostMapping(
            value = "/agent",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> agentRegister(
            @Parameter(
                    name = "agentCertificationDocuments",
                    description = "배열로 보내줄 것(1. 중개 사무소 등록증, 2. 사업자 등록증, 3. 공인중개사 자격증)"
            )
            @RequestPart("agentCertificationDocuments") List<MultipartFile> agentCertificationDocuments,
            @Parameter(
                    name = "agentImage",
                    description = "공인 중개사 본인 인증 사진"
            )
            @RequestPart("agentImage") MultipartFile agentImage,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "공인중개사 추가 정보 요청 데이터(JSON)",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AgentUserRequest.class)
                    )
            )
            @RequestPart(value = "agentUserRequest") String agentUserRequest
            ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        AgentUserRequest agentUserRequests = objectMapper.readValue(agentUserRequest, AgentUserRequest.class);
        afterOAuthService.agentRegisters(agentUserRequests, agentCertificationDocuments, agentImage);
        return ResponseEntity.ok().build();
    }
}
