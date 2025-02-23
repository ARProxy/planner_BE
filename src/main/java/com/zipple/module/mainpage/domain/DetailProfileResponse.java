package com.zipple.module.mainpage.domain;

import com.zipple.module.mypage.agent.portfolio.domain.PortfolioProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "공인중개사 상세 프로필")
public class DetailProfileResponse {

    private String title;
    private String externalLink;
    private String agentName;
    private String businessName;
    private String agentSpecialty;
    private String agentRegistrationNumber;
    private String ownerName;
    private String ownerContactNumber;
    private String officeAddress;
    private List<PortfolioProfile> portfolios;
}
