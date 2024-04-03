package org.kb.board.security.handler;

import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kb.board.util.JWTUtil;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
// 인증 성공 후 수행할 작업
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("로그인 성공");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info("로그인 한 유저 이름: {}", authentication.getName());

        // 토큰 생성 정보 생성
        Map<String, Object> claim = Map.of("user_id", authentication.getName());

        // 토큰 생성
        String accessToken = jwtUtil.generateToken(claim, 10); // access token
        String refreshToken = jwtUtil.generateToken(claim, 100); // refresh token

        // 생성한 토큰을 하나의 문자열로 변경
        Gson gson = new Gson();
        Map<String, Object> keyMap = Map.of("accessToken", accessToken, "refreshToken", refreshToken);

        String jsonStr = gson.toJson(keyMap);
        // 클라이언트에게 전송
        response.getWriter().println(jsonStr);
    }
}
