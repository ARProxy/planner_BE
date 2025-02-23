package com.zipple.module.member.oauth.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "agentUserRequest", description = "공인중개사 요청 데이터")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentUserRequest {

    @Schema(description = "공인중개사 구분", example = "소속/개업")
    private String agentType;

    @Schema(description = "전문 분야", example = "오피스텔")
    private String agentSpecialty;

    @Schema(description = "상호명")
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

    @Schema(description = "소속 공인중개사 이름", example = "권동휘")
    private String agentName;

    @Schema(description = "소속 공인중개사 연락처", example = "010-1111-1111")
    private String agentContactNumber;

    @Schema(description = "1인 가구 전문가 신청", example = "false")
    private Boolean singleHouseholdExpertRequest;

    @Schema(description = "자기 소개글 제목")
    private String introductionTitle;

    @Schema(description = "자기 소개글 내용")
    private String introductionContent;

    @Schema(description = "외부링크", example = "https://dong.com")
    private String externalLink;

    @Schema(description = "마케팅 수신 동의", example = "false")
    private Boolean marketingNotificationTerms;

}
