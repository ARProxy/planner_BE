package com.zipple.module.mypage.general.domain;

import com.zipple.module.member.common.entity.category.HousingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyPageResponse {

    private String email;
    private String generalName;
    private String generalAddress;
    private String housingType;
    private LocalDateTime createTime;

    public MyPageResponse(String email, String generalName, String generalAddress, HousingType housingType, LocalDateTime createTime) {
        this.email = email;
        this.generalName = generalName;
        this.generalAddress = generalAddress;
        this.housingType = (housingType != null) ? housingType.getHousingType() : null; // 한글로 변환
        this.createTime = createTime;
    }

}
