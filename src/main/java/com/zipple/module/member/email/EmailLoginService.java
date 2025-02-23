package com.zipple.module.member.email;

import com.zipple.common.auth.jwt.token.AuthTokens;
import com.zipple.common.auth.jwt.token.AuthTokensGenerator;
import com.zipple.module.member.common.entity.User;
import com.zipple.module.member.common.entity.category.AgentType;
import com.zipple.module.member.common.repository.UserRepository;
import com.zipple.module.member.email.model.EmailLoginRequest;
import com.zipple.module.member.email.model.EmailLoginResponse;
import com.zipple.module.member.email.model.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailLoginService {

    private final PasswordEncoder passwordEncoder;
    private final AuthTokensGenerator authTokensGenerator;
    private final UserRepository userRepository;

    @Transactional
    public EmailLoginResponse emailLogin(EmailLoginRequest emailLoginRequest) {
        User user = userRepository.findByEmail(emailLoginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + emailLoginRequest.getEmail()));

        if (passwordEncoder.matches(emailLoginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        UserType userType = determineUserType(user);
        log.info("User [{}] is classified as [{}]", user.getEmail(), userType);
        String accessToken = authTokensGenerator.generate(user.getId()).getAccessToken();

        return new EmailLoginResponse(accessToken, userType);
    }

    private UserType determineUserType(User user) {

        if(user.getAgentUser() == null) {
            return UserType.GENERAL;
        } else {
            AgentType agentType = user.getAgentUser().getAgentType();
            return switch (agentType) {
                case BUSINESS_AGENT -> UserType.BUSINESS_AGENT;
                case AFFILIATED_AGENT -> UserType.AFFILIATED_AGENT;
            };
        }
    }
}
