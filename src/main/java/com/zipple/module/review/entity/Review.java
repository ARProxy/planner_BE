package com.zipple.module.review.entity;

import com.zipple.module.member.common.entity.AgentUser;
import com.zipple.module.member.common.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private AgentUser agentUser;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer starCount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public void updateReview(String content, int starCount) {
        this.content = content;
        this.starCount = starCount;
        this.updatedAt = LocalDateTime.now();
    }
}
