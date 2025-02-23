package com.zipple.module.member.common.entity.category;

import lombok.Getter;

@Getter
public enum AgentType {
    BUSINESS_AGENT("개업"),
    AFFILIATED_AGENT("소속");

    private final String description;

    AgentType(String description) {
        this.description = description;
    }

    public static AgentType fromValue(String value) {
        for (AgentType type : AgentType.values()) {
            if (type.getDescription().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Housing Type: " + value);
    }
}
