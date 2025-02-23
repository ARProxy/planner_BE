package com.zipple.module.mypage.agent.portfolio.domain;

import com.zipple.module.member.common.entity.category.AgentSpecialty;
import com.zipple.module.member.common.entity.category.AgentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageAgentAllResponse {

    private String email;
    private AgentType agentType;
    private AgentSpecialty agentSpecialty;
    private String businessName;
    private String agentRegistrationNumber;
    private String primaryContactNumber;
    private String officeAddress;
    private String ownerName;
    private String ownerContactNumber;
    private String agentName;
    private String agentContactNumber;
    private Boolean singleHouseholdExpertRequest;
    private String agentOfficeRegistrationCertificate;
    private String businessRegistrationCertification;
    private String agentLicense;
    private String agentImage;
    private String title;
    private String content;
    private String externalLink;
}
