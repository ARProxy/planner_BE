package com.zipple.module.member.email.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "EmailAgentRequest")
public class EmailAgentRequest {

    @Schema(description = "공인중개사 구분", example = "소속 or 개업")
    private String agentType;

    @Schema(description = "전문 분야", example = "아파트")
    private String agentSpecialty;

    @Schema(description = "이메일", example = "kakao@kakao.com")
    private String email;

    @Schema(description = "비밀번호", example = "dong1234")
    private String password;

    @Schema(description = "상호명", example = "회사")
    private String businessName;

    @Schema(description = "중개 등록번호", example = "12-3214-213124")
    private String agentRegistrationNumber;

    @Schema(description = "대표 전화 번호", example = "010-1111-1111")
    private String primaryContactNumber;

    @Schema(description = "사업장 주소", example = "서울특별시 역삼3동")
    private String officeAddress;

    @Schema(description = "대표자 성함", example = "권동휘")
    private String ownerName;

    @Schema(description = "대표자 연락처", example = "010-1111-1111")
    private String ownerContactNumber;

    @Schema(description = "1인 가구 전문가 신청", example = "true")
    private Boolean singleHouseholdExpertRequest;

    @Schema(description = "소속 공인중개사 이름", example = "권동휘")
    private String agentName;

    @Schema(description = "소속 공인중개사 연락처", example = "010-1111-1111")
    private String agentContactNumber;

    @Schema(description = "자기 소개글 제목", example = "자기 소개")
    private String introductionTitle;

    @Schema(description = "자기 소개글 내용", example = "자기 소개글")
    private String introductionContent;

    @Schema(description = "외부링크", example = "http://link")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String externalLink;

    @Schema(description = "마케팅 수신 동의", example = "true")
    private Boolean marketingNotificationTerms;
}
