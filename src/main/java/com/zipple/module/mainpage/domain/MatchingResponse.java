package com.zipple.module.mainpage.domain;

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
@Schema(description = "매칭 데이터")
public class MatchingResponse {
    @Schema(description = "매칭 데이터 리스트")
    private List<AgentMatchingResponse> matching;

    @Schema(description = "총 개수", example = "20")
    private Long totalElements;

    @Schema(description = "총 페이지 수", example = "3")
    private Integer totalPages;

    @Schema(description = "현재 페이지 번호", example = "0")
    private Integer currentPage;

    @Schema(description = "마지막 페이지 여부", example = "false")
    private Boolean isLast;
}
