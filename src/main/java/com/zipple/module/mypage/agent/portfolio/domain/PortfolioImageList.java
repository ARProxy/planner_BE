package com.zipple.module.mypage.agent.portfolio.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Schema(name = "portfolioImages")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortfolioImageList {

    @Schema(description = "포트폴리오 이미지")
    private List<MultipartFile> portfolioImages;
}
