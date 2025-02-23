package com.zipple.module.mypage.agent;

import com.zipple.module.member.common.entity.category.AgentType;
import com.zipple.module.mypage.agent.domain.BasicUpdateRequest;
import com.zipple.module.mypage.agent.domain.DetailUpdateRequest;
import com.zipple.module.mypage.agent.domain.MyPageAgentResponse;
import com.zipple.module.mypage.agent.portfolio.domain.MyPageAgentAllResponse;
import com.zipple.module.mypage.agent.portfolio.domain.PortfolioImageList;
import com.zipple.module.mypage.agent.portfolio.domain.PortfolioPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "공인중개사 마이페이지")
@RequestMapping(value = "/api/v1/mypage/agent")
@RestController
@RequiredArgsConstructor
public class MyPageAgentController {

    private final MyPageAgentService myPageAgentService;

    @Operation(summary = "공인중개사 정보 조회")
    @GetMapping
    public ResponseEntity<MyPageAgentResponse> getLicensedAgent() {
        MyPageAgentResponse response = myPageAgentService.getLicensedAgent();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "공인중개사 수정화면 정보 조회")
    @GetMapping(value = "/all")
    public ResponseEntity<MyPageAgentAllResponse> getAgentAllInfo() {
        return ResponseEntity.ok(myPageAgentService.getAgentAllInfo());
    }

    @Operation(summary = "공인중개사 기본 정보 수정")
    @PutMapping
    public ResponseEntity<String> updateLicensedAgent(
            @RequestBody BasicUpdateRequest basicUpdateRequest
    ) {
        myPageAgentService.updateLicensedAgent(basicUpdateRequest);
        return ResponseEntity.ok("개업 공인중개사 정보 수정 완료");
    }

    @Operation(summary = "공인중개사 상세 정보 수정")
    @PutMapping(value = "/detail")
    public ResponseEntity<String> updateLicensedAgentDetail(
            @RequestBody DetailUpdateRequest detailUpdateRequest
    ) {
        myPageAgentService.updateLicensedAgentDetail(detailUpdateRequest);
        return ResponseEntity.ok("상세 정보 수정 완료");
    }

    @Operation(summary = "포트폴리오 조회")
    @GetMapping(value = "/portfolio")
    public ResponseEntity<PortfolioPageResponse> getLicensedAgentPortfolio(
            @Parameter(name = "agentType")
            @RequestParam(value = "agentType") String agentType,
            @Parameter(name = "page")
            @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(name = "size")
            @RequestParam(value = "size", defaultValue = "9") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        AgentType type = AgentType.fromValue(agentType);
        PortfolioPageResponse portfolios = myPageAgentService.getLicensedAgentPortfolios(pageable, type);
        return ResponseEntity.ok(portfolios);
    }

    @Operation(summary = "포트폴리오 생성")
    @PostMapping(value = "/portfolio",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createLicensedAgentPortfolio(
            @Parameter(name = "portfolioTitle", description = "포트폴리오 제목")
            @RequestParam(value = "portfolioTitle") String portfolioTitle,
            @Parameter(name = "agentType", description = "소속 또는 개업 (BUSINESS_AGENT, AFFILIATED_AGENT)")
            @RequestParam(value = "agentType") String agentType,
            @Parameter(name = "portfolioImages", description = "포트폴리오 사진 리스트로 받을거에요")
            @ModelAttribute PortfolioImageList portfolioImages
            ) {
        AgentType type = AgentType.fromValue(agentType);
        myPageAgentService.createLicensedPortfolio(portfolioImages, portfolioTitle, type);
        return ResponseEntity.ok("포트폴리오 생성 완료");
    }

}
