package org.kb.board.security.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;


// 로그인을 위한 Security 필터 적용
// 로그인을 성공하면 Access Token과 Refresh Token을 생성
// Token을 이용해서 Controller에게 요청을 전송할 때 인증과 권한을 체크하는 기능
// Token을 발급하는 기능은 Controller 클래스에게 위임을 해도 되지만 Spring Secuirty의 AbstractAuthenticationProcessingFilter 클래스를 권장
@Slf4j
public class APILoginFilter extends AbstractAuthenticationProcessingFilter {
    public APILoginFilter(String defaultFilterProcessingUrl) {
        super(defaultFilterProcessingUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("API LoginFilter -------------------");
        return null;
    }
}
