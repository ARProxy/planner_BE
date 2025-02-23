package com.zipple.module.member.common.entity.category;

import lombok.Getter;

@Getter
public enum HousingType {

    APARTMENT("아파트"),
    OFFICETEL("오피스텔"),
    VILLA("빌라"),
    ONE_OR_TWO_ROOM("원룸/투룸"),
    ROW_HOUSE("연립"),
    DETACHED_HOUSE("주택"),
    MULTI_FAMILY("다가구"),
    MULTI_HOUSEHOLD("다세대"),
    OTHERS("기타");

    private final String housingType;

    HousingType(String housingType) {
        this.housingType = housingType;
    }

    public static HousingType fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Housing Type cannot be null or empty");
        }

        String normalizedValue = value.trim();
        for (HousingType type : HousingType.values()) {
            if (type.getHousingType().equalsIgnoreCase(normalizedValue)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Housing Type: " + value);
    }
}
