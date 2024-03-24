package org.kb.board.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class CustomSecurityConfig {

    // Spring Security에서 제공하는 인증, 인가를 위한 필터들의 모음.
    // http 요청 -> servlet contianer -> filter1 -> filter2 -> ... -> servlet -> controller
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("필터 환경 설정");
        return http.build();
    }

    // PasswordEncoder 빈을 생성
    // 복호화가 불가능한 암호화를 수행해주는 클래스
    // 복호화는 불가능하지만 비교는 가능.
    // SpringBoot에서는 내부적으로 BCryptPasswordEncoder를 사용
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
