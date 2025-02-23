package com.zipple.module.mypage.agent.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailUpdateRequest {
    @Schema(description = "사업장 이름", example = "집플 공인중개사")
    private String businessName;

    @Schema(description = "중개 등록번호", example = "12345-6789-01234")
    private String agentRegistrationNumber;

    @Schema(description = "대표 전화번호", example = "02-1234-5678")
    private String primaryContactNumber;

    @Schema(description = "사업장 주소", example = "서울특별시 서초구 서초대로 46길 99")
    private String officeAddress;

    @Schema(description = "1인 가구 전문가 신청", example = "false")
    private Boolean singleHouseholdExpertRequest;

    @Schema(description = "대표자 이름", example = "권동휘")
    private String ownerName;
}
