package org.kb.board.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kb.board.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class JWTUtil {
    @Value("${com.kb.board.secret}")
    private String key;

    private final UserServiceImpl userService;

    // 토큰 생성
    public String generateToken(Map<String, Object> valueMap, int days) {
        log.info("토큰 생성");

        // 헤더 생성
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        // payload 생성
        Map<String, Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);

        // 유효시간
        int time = (1) * days;

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(time).toInstant()))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();
        return jwtStr;
    }

    // 토큰의 유효성 검사 메서드
    public Map<String, Object> validateToken(String token) {
        Map<String, Object> claim = null;

        claim = Jwts.parser()
                .setSigningKey(key.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return claim;
    }

//    public UserDetails getUsername(String accessToken) {
//        try {
//            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(accessToken).getBody();
//            // 토큰에서 사용자 정보 추출
//            String username = claims.getSubject();
//            log.info("claims: " + claims);
//            // 필요한 경우, 추가적인 검증 로직 수행 가능
//            // 예: 토큰의 만료 시간 검증 등
//            // ...
//
//            // UserDetails 객체 생성 후 반환
//            UserDetails userDetails = userService.loadUserByUsername(username); // CustomUserDetails는 UserDetails의 구현체로서 사용자 정보를 담고 있음
//            return userDetails;
//        } catch (Exception e) {
//            // 토큰 검증 실패
//            return null;
//        }
//    }
}
