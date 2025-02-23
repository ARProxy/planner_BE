package com.zipple.module.mainpage;

import com.zipple.module.mainpage.domain.DetailProfileResponse;
import com.zipple.module.mainpage.domain.MatchingResponse;
import com.zipple.module.member.common.entity.category.AgentType;
import com.zipple.module.mypage.agent.portfolio.domain.PortfolioPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "메인 화면")
@RestController
@RequestMapping(value = "/api/v1/main")
@RequiredArgsConstructor
public class MainPageController {

    private final MainPageService mainPageService;

    @Operation(summary = "메인은 일단 프론트에서 뿌릴까요")
    @GetMapping
    public ResponseEntity<Void> getMainPage() {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "매칭 프로필 기본 화면")
    @GetMapping(value = "/matching")
    public ResponseEntity<MatchingResponse> getMatchingProfile(
            @Parameter(name = "page")
            @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(name = "size")
            @RequestParam(value = "size", defaultValue = "9") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        MatchingResponse matchingResponse = mainPageService.getMatchingProfile(pageable);
        return ResponseEntity.ok(matchingResponse);
    }

    @Operation(summary = "공인 중개사 상세 프로필")
    @GetMapping(value = "/profile/detail")
    public ResponseEntity<DetailProfileResponse> getAgentDetailProfile(
            @Parameter(name = "userId") @RequestParam(value = "userId") Long userId
    ) {
        DetailProfileResponse detailProfileResponse = mainPageService.getAgentDetailProfile(userId);
        return ResponseEntity.ok(detailProfileResponse);
    }

    @Operation(summary = "공인 중개사 포트폴리오")
    @GetMapping(value = "/portfolio")
    public ResponseEntity<PortfolioPageResponse> getAgentPortfolio(
            @Parameter(name = "userId")
            @RequestParam(value = "userId") Long userId,
            @Parameter(name = "agentType")
            @RequestParam(value = "agentType") String agentType,
            @Parameter(name = "page")
            @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(name = "size")
            @RequestParam(value = "size", defaultValue = "9") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        AgentType type = AgentType.fromValue(agentType);
        PortfolioPageResponse portfolios = mainPageService.getAgentPortfolio(userId, pageable, type);

        return ResponseEntity.ok(portfolios);
    }
}
