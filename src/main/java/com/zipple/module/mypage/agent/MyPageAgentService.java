package com.zipple.module.mypage.agent;

import com.zipple.common.utils.GetMember;
import com.zipple.module.member.common.entity.AgentUser;
import com.zipple.module.member.common.entity.User;
import com.zipple.module.member.common.entity.category.AgentType;
import com.zipple.module.member.common.repository.AgentUserRepository;
import com.zipple.module.member.common.repository.UserRepository;
import com.zipple.module.mypage.agent.domain.BasicUpdateRequest;
import com.zipple.module.mypage.agent.domain.DetailUpdateRequest;
import com.zipple.module.mypage.agent.domain.MyPageAgentResponse;
import com.zipple.module.mypage.agent.portfolio.Portfolio;
import com.zipple.module.mypage.agent.portfolio.PortfolioImage;
import com.zipple.module.mypage.agent.portfolio.PortfolioImageRepository;
import com.zipple.module.mypage.agent.portfolio.PortfolioRepository;
import com.zipple.module.mypage.agent.portfolio.domain.MyPageAgentAllResponse;
import com.zipple.module.mypage.agent.portfolio.domain.PortfolioImageList;
import com.zipple.module.mypage.agent.portfolio.domain.PortfolioMainImage;
import com.zipple.module.mypage.agent.portfolio.domain.PortfolioPageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageAgentService {

    private final GetMember getMember;
    private final AgentUserRepository agentUserRepository;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final PortfolioImageRepository portfolioImageRepository;

    @Transactional(readOnly = true)
    public MyPageAgentResponse getLicensedAgent() {
        User currentUser = getMember.getCurrentMember();

        AgentUser agentUser = currentUser.getAgentUser();
        if (agentUser == null) {
            throw new IllegalArgumentException("Agent 정보가 없습니다.");
        }

        return MyPageAgentResponse.builder()
                .agentName(agentUser.getAgentName())
                .ownerName(agentUser.getOwnerName())
                .agentType(agentUser.getAgentType().getDescription())
                .phoneNumber(agentUser.getPrimaryContactNumber())
                .email(currentUser.getEmail())
                .createAt(currentUser.getUpdatedAt().toString())
                .build();
    }

    @Transactional
    public void updateLicensedAgent(BasicUpdateRequest request) {
        User currentUser = getMember.getCurrentMember();

        AgentUser agentUser = agentUserRepository.findById(currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("AgentUser 정보를 찾을 수 없습니다."));

        agentUser.setPrimaryContactNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : agentUser.getPrimaryContactNumber());
        agentUser.setIntroductionTitle(request.getTitle() != null ? request.getTitle() : agentUser.getIntroductionTitle());
        agentUser.setIntroductionContent(request.getContent() != null ? request.getContent() : agentUser.getIntroductionContent());
        agentUser.setExternalLink(request.getExternalLink() != null ? request.getExternalLink() : agentUser.getExternalLink());

        currentUser.setEmail(request.getEmail());
        userRepository.save(currentUser);
        agentUserRepository.save(agentUser);
    }

    @Transactional
    public void updateLicensedAgentDetail(DetailUpdateRequest request) {
        User currentUser = getMember.getCurrentMember();

        AgentUser agentUser = agentUserRepository.findById(currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("AgentUser 정보를 찾을 수 없습니다."));

        agentUser.setBusinessName(request.getBusinessName() != null ? request.getBusinessName() : agentUser.getBusinessName());
        agentUser.setAgentRegistrationNumber(request.getAgentRegistrationNumber() != null ? request.getAgentRegistrationNumber() : agentUser.getAgentRegistrationNumber());
        agentUser.setPrimaryContactNumber(request.getPrimaryContactNumber() != null ? request.getPrimaryContactNumber() : agentUser.getPrimaryContactNumber());
        agentUser.setOfficeAddress(request.getOfficeAddress() != null ? request.getOfficeAddress() : agentUser.getOfficeAddress());
        agentUser.setSingleHouseholdExpertRequest(request.getSingleHouseholdExpertRequest());

        agentUserRepository.save(agentUser);
    }

    @Transactional
    public void createLicensedPortfolio(PortfolioImageList portfolioImages, String portfolioTitle, AgentType agentType) {
        User currentUser = getMember.getCurrentMember();

        Portfolio portfolio = Portfolio.builder()
                .user(currentUser)
                .title(portfolioTitle)
                .agentType(agentType)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<PortfolioImage> savedImages = new ArrayList<>();
        String baseDir = "/home/ubuntu/zipple/upload";
        String baseUrl = "http://44.203.190.167:8081";

        for (int i = 0; i < portfolioImages.getPortfolioImages().size(); i++) {
            MultipartFile image = portfolioImages.getPortfolioImages().get(i);
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            String filePath = baseDir + "/" + fileName;

            try {
                File destFile = new File(filePath);
                image.transferTo(destFile);

                String imageUrl = baseUrl + "/zipple/" + fileName;

                PortfolioImage portfolioImage = PortfolioImage.builder()
                        .portfolio(portfolio)
                        .imageUrl(imageUrl)
                        .isMain(i == 0)
                        .createdAt(LocalDateTime.now())
                        .build();

                savedImages.add(portfolioImage);

            } catch (IOException e) {
                throw new IllegalStateException("이미지 저장 중 오류 발생: " + image.getOriginalFilename(), e);
            }
        }

        portfolio.setPortfolioImage(savedImages);
        portfolioRepository.save(portfolio);
    }
    @Transactional(readOnly = true)
    public PortfolioPageResponse getLicensedAgentPortfolios(Pageable pageable, AgentType agentType) {
        User user = getMember.getCurrentMember();
        long userId = user.getId();
        Page<PortfolioMainImage> page = portfolioImageRepository.findMainImagesWithPagination(userId, pageable, agentType);

        return PortfolioPageResponse.builder()
                .content(page.getContent())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber())
                .isLast(page.isLast())
                .build();
    }

    @Transactional(readOnly = true)
    public MyPageAgentAllResponse getAgentAllInfo() {
        User user = getMember.getCurrentMember();

        AgentUser agentUser = agentUserRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("공인중개사 정보가 존재하지 않습니다."));

        return MyPageAgentAllResponse.builder()
                .email(user.getEmail())
                .agentType(agentUser.getAgentType())
                .agentSpecialty(agentUser.getAgentSpecialty())
                .businessName(agentUser.getBusinessName())
                .agentRegistrationNumber(agentUser.getAgentRegistrationNumber())
                .primaryContactNumber(agentUser.getPrimaryContactNumber())
                .officeAddress(agentUser.getOfficeAddress())
                .ownerName(agentUser.getOwnerName())
                .ownerContactNumber(agentUser.getOwnerContactNumber())
                .agentName(agentUser.getAgentName())
                .agentContactNumber(agentUser.getAgentContactNumber())
                .singleHouseholdExpertRequest(agentUser.getSingleHouseholdExpertRequest())
                .agentOfficeRegistrationCertificate(agentUser.getAgentOfficeRegistrationCertificate())
                .businessRegistrationCertification(agentUser.getBusinessRegistrationCertification())
                .agentLicense(agentUser.getAgentLicense())
                .agentImage(agentUser.getAgentImage())
                .title(agentUser.getIntroductionTitle())
                .content(agentUser.getIntroductionContent())
                .externalLink(agentUser.getExternalLink())
                .build();
    }
}

