package com.zipple.module.mainpage;

import com.zipple.common.utils.AgentIdBase64Util;
import com.zipple.module.like.entity.AgentLikeRepository;
import com.zipple.module.mainpage.domain.*;
import com.zipple.module.member.common.entity.AgentUser;
import com.zipple.module.member.common.entity.User;
import com.zipple.module.member.common.entity.category.AgentSpecialty;
import com.zipple.module.member.common.entity.category.AgentType;
import com.zipple.module.member.common.repository.AgentUserRepository;
import com.zipple.module.member.common.repository.UserRepository;
import com.zipple.module.mypage.agent.portfolio.Portfolio;
import com.zipple.module.mypage.agent.portfolio.PortfolioImage;
import com.zipple.module.mypage.agent.portfolio.PortfolioImageRepository;
import com.zipple.module.mypage.agent.portfolio.PortfolioRepository;
import com.zipple.module.mypage.agent.portfolio.domain.PortfolioMainImage;
import com.zipple.module.mypage.agent.portfolio.domain.PortfolioPageResponse;
import com.zipple.module.mypage.agent.portfolio.domain.PortfolioProfile;
import com.zipple.module.review.entity.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainPageService {

    private final AgentUserRepository agentUserRepository;
    private final PortfolioRepository portfolioRepository;
    private final PortfolioImageRepository portfolioImageRepository;
    private final UserRepository userRepository;
    private final AgentLikeRepository likeRepository;
    private final ReviewRepository reviewRepository;
    private final AgentIdBase64Util agentIdUUIDUtil;

    @Transactional(readOnly = true)
    public MatchingResponse getMatchingProfile(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        List<User> users = userPage.getContent();
        List<AgentMatchingResponse> matchingList = new ArrayList<>();

        for(User user : users) {
            Long userId = user.getId();
            int portfolioCount = portfolioRepository.countByUserId(userId);
            Optional<AgentUser> optionalAgentUser = agentUserRepository.findById(userId);

            if (optionalAgentUser.isPresent()) {
                AgentUser agentUser = optionalAgentUser.get();
                String agentSpecialty = AgentSpecialty.getDescriptionByAgentSpecialty(agentUser.getAgentSpecialty());

                Integer likeCount = likeRepository.countByAgentUserId(userId);
                Integer reviewCount = reviewRepository.countByAgentUser(agentUser);
                Double startRating = reviewRepository.findAverageStarCountByAgent(agentUser.getId());
                AgentMatchingResponse agentMatchingResponse = AgentMatchingResponse.builder()
                        .agentId(agentIdUUIDUtil.encodeLong(userId))
                        .profileUrl(user.getProfile_image_url())
                        .agentSpecialty(agentSpecialty)
                        .portfolioCount(portfolioCount)
                        .agentName(agentUser.getAgentName())
                        .title(agentUser.getIntroductionTitle())
                        .starRating(startRating == null ? 0.0 : startRating)
                        .likeCount(likeCount)
                        .reviewCount(reviewCount)
                        .singleHouseholdExpert(agentUser.getSingleHouseholdExpertRequest())
                        .build();
                matchingList.add(agentMatchingResponse);
            }
        }

        return MatchingResponse.builder()
                .matching(matchingList)
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .currentPage(userPage.getNumber())
                .isLast(userPage.isLast())
                .build();
    }

    @Transactional(readOnly = true)
    public DetailProfileResponse getAgentDetailProfile(String agentId) {
        Long userId = agentIdUUIDUtil.decodeLong(agentId);
        AgentUser agentUser = agentUserRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        User user = agentUser.getUser();

        List<Portfolio> portfolios = portfolioRepository.findTop6ByUserIdOrderByCreatedAtDesc(userId);

        List<PortfolioProfile> portfolioProfiles = portfolios.stream()
                .map(portfolio -> {
                    Optional<PortfolioImage> mainImage = portfolio.getPortfolioImage().stream()
                            .filter(PortfolioImage::getIsMain)
                            .findFirst();

                    return PortfolioProfile.builder()
                            .portfolioId(portfolio.getId())
                            .title(portfolio.getTitle())
                            .createdAt(portfolio.getCreatedAt().toString())
                            .portfolioImage(mainImage.map(PortfolioImage::getImageUrl).toString())
                            .build();
                })
                .collect(Collectors.toList());

        Double startRating = reviewRepository.findAverageStarCountByAgent(agentUser.getId());
        return DetailProfileResponse.builder()
                .email(user.getEmail())
                .profileUrl(user.getProfile_image_url())
                .title(user.getNickname())
                .externalLink(agentUser.getExternalLink())
                .agentName(agentUser.getAgentName())
                .starRating(startRating == null ? 0.0 : startRating)
                .businessName(agentUser.getBusinessName())
                .agentSpecialty(AgentSpecialty.getDescriptionByAgentSpecialty(agentUser.getAgentSpecialty()))
                .agentRegistrationNumber(agentUser.getAgentRegistrationNumber())
                .ownerName(agentUser.getOwnerName())
                .ownerContactNumber(agentUser.getOwnerContactNumber())
                .officeAddress(agentUser.getOfficeAddress())
                .portfolios(portfolioProfiles)
                .build();
    }

    @Transactional(readOnly = true)
    public PortfolioPageResponse getAgentPortfolio(String agentId, Pageable pageable) {
        Long userId = agentIdUUIDUtil.decodeLong(agentId);
        AgentUser agentUser = agentUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공인중개사입니다."));

        AgentType agentType = agentUser.getAgentType();

        Page<PortfolioMainImage> page = portfolioImageRepository.findMainImagesByUserIdWithPagination(userId, agentType, pageable);

        return PortfolioPageResponse.builder()
                .content(page.getContent())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber())
                .isLast(page.isLast())
                .build();
    }

    @Transactional(readOnly = true)
    public MatchingResponse getMatchingCategory(String category, Pageable pageable) {
        AgentSpecialty agentSpecialty = AgentSpecialty.getByDescription(category);

        Page<AgentUser> agentUserPage = agentUserRepository.findByAgentSpecialty(agentSpecialty, pageable);
        List<AgentUser> agentUsers = agentUserPage.getContent();
        List<AgentMatchingResponse> matchingList = new ArrayList<>();

        for (AgentUser agentUser : agentUsers) {
            Long userId = agentUser.getId();
            User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
            Integer portfolioCount = portfolioRepository.countByUserId(userId);
            Integer likeCount = likeRepository.countByAgentUserId(userId);
            Integer reviewCount = reviewRepository.countByAgentUser(agentUser);
            Double startRating = 0.0;
            try {
                startRating = reviewRepository.findAverageStarCountByAgent(agentUser.getId());
            } catch (Exception e) {
                Throwable cause = e.getCause();
                log.error(cause.getMessage(), cause);
            }

            AgentMatchingResponse agentMatchingResponse = AgentMatchingResponse.builder()
                    .agentId(agentIdUUIDUtil.encodeLong(userId))
                    .profileUrl(user.getProfile_image_url())
                    .agentSpecialty(category)
                    .portfolioCount(portfolioCount)
                    .agentName(agentUser.getAgentName())
                    .title(agentUser.getIntroductionTitle())
                    .starRating(startRating)
                    .likeCount(likeCount)
                    .reviewCount(reviewCount)
                    .singleHouseholdExpert(agentUser.getSingleHouseholdExpertRequest())
                    .build();
            matchingList.add(agentMatchingResponse);
        }


        return MatchingResponse.builder()
                .matching(matchingList)
                .totalElements(agentUserPage.getTotalElements())
                .totalPages(agentUserPage.getTotalPages())
                .currentPage(agentUserPage.getNumber())
                .isLast(agentUserPage.isLast())
                .build();
    }

    public DetailPortfolioResponse getAgentPortfolioDetail(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findByIdWithImages(portfolioId)
                .orElseThrow(() -> new EntityNotFoundException("포트폴리오를 찾을 수 없습니다. ID: " + portfolioId));

        List<String> imageUrls = portfolio.getPortfolioImage().stream()
                .sorted(Comparator.comparing(PortfolioImage::getIsMain).reversed())
                .map(image -> Optional.ofNullable(image.getImageUrl()).orElse(""))
                .toList();

        String externalLink = Optional.ofNullable(portfolio.getUser().getAgentUser())
                .map(AgentUser::getExternalLink)
                .orElse("");

        return DetailPortfolioResponse.builder()
                .title(portfolio.getTitle())
                .externalLink(externalLink)
                .content(portfolio.getContent())
                .portfolioList(imageUrls)
                .build();
    }
}
