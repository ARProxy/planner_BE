package com.zipple.common.auth.jwt;

import com.zipple.module.member.common.entity.User;
import com.zipple.module.member.common.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final UserRepository userRepository;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, UserRepository userRepository) {
        this.userRepository = userRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generate(String subject, Date expiredAt) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractSubject(String accessToken) {
        Claims claims = parseClaims(accessToken);
        return claims.getSubject();
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            return true;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String subject = claims.getSubject();
        User user = new User();
        Optional<User> memberInfo = userRepository.findById(Long.valueOf(subject));
        UserDetails userDetails = loadUserDetails(memberInfo.get().getEmail());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private UserDetails loadUserDetails(String subject) {
        userRepository.findByEmail(subject)
                .orElseThrow(() -> new UsernameNotFoundException("유저 없다"));
        return new org.springframework.security.core.userdetails.User(subject, "", Collections.emptyList());
    }

    public long getRemainingExpiration(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            long expMillis = claims.getExpiration().getTime();
            long nowMillis = System.currentTimeMillis();
            return expMillis - nowMillis;
        } catch (JwtException e) {
            return 0;
        }
    }

    public String getRemainingTime(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            long expirationTimeMillis = claims.getExpiration().getTime();
            long currentTimeMillis = System.currentTimeMillis();
            long remainingTimeMillis = expirationTimeMillis - currentTimeMillis;

            long minutes = (remainingTimeMillis / 1000) / 60;
            long seconds = (remainingTimeMillis / 1000) % 60;

            return String.format("%d분 %d초", minutes, seconds);
        } catch (ExpiredJwtException e) {
            return "토큰이 만료되었습니다.";
        }
    }

}
