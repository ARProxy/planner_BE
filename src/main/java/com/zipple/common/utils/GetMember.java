package com.zipple.common.utils;

import com.zipple.module.member.common.entity.User;
import com.zipple.module.member.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetMember {

    private final UserRepository userRepository;

    public User getCurrentMember() {
        return userRepository
                .findByEmail(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName()
                )
                .orElseThrow(() -> new IllegalStateException("다시 로그인 해주세요"));
    }
}
