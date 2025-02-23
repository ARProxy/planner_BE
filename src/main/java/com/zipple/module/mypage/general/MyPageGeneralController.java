package com.zipple.module.mypage.general;

import com.zipple.module.mypage.general.domain.MyPageResponse;
import com.zipple.module.mypage.general.domain.MyPageUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "일반 회원 마이페이지")
@RequestMapping(value = "/api/v1/mypage/general")
@RestController
@RequiredArgsConstructor
public class MyPageGeneralController {

    private final MyPageGeneralService myPageGeneralService;

    @Operation(
            summary = "일반 사용자 마이페이지 정보 조회"
    )
    @GetMapping()
    public ResponseEntity<MyPageResponse> getInformation() {
        MyPageResponse myPageResponse = myPageGeneralService.getMyPageInformation();
        return ResponseEntity.ok(myPageResponse);
    }

    @Operation(
            summary = "일반 사용자 마이페이지 정보 수정"
    )
    @PutMapping()
    public ResponseEntity<String> updateInformation(
            @RequestBody MyPageUpdateRequest myPageUpdateRequest
    ) {
        myPageGeneralService.updateMyPageInformation(myPageUpdateRequest);
        return ResponseEntity.ok("정보 수정이 완료되었습니다.");
    }

    @Operation(
            summary = "일반 사용자 마이페이지 회원 탈퇴"
    )
    @DeleteMapping()
    public ResponseEntity<String> deleteInformation() {
        myPageGeneralService.deleteMyPageUser();
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }

    @Operation(
            summary = "찜 일단 보류"
    )
    @GetMapping(
            value = "/zzim"
    )
    public ResponseEntity<Void> getZzim() {
        return ResponseEntity.ok().build();
    }

}
