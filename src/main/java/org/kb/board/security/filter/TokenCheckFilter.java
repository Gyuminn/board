package org.kb.board.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kb.board.util.JWTUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    // 필터로 동작하는 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 클라이언트의 URI 확인
        String path = request.getRequestURI();

        // '/v1'로 시작하지 않는 요청의 경우는 다음 필터로 넘기는데
        // 다음 필터로 넘길 때 반드시 return을 해야 한다.
        // return을 만날 때까지 무조건 수행
        if (!path.startsWith("/v1")) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Token Check Filter..............");
        log.info("JWTUtil: {}", jwtUtil);

        filterChain.doFilter(request, response);
    }



}
