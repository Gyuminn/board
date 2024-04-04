package org.kb.board.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;


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

        // GET 방식일 경우에 처리할 필요가 없다.
        if (request.getMethod().equalsIgnoreCase("GET")) {
            log.info("GET Method Not Supported");
            return null;
        }

        // 토큰 생성 요청을 했을 때 아이디와 비밀번호를 가져와서 Map으로 생성
        Map<String, String> jsonData = parseRequestJSON(request);
        log.info("jsonData: {}", jsonData);

        // 아이디와 비밀번호를 다음 필터에 전송해서 사용하도록 설정
        // UserService 동작
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                jsonData.get("user_id"), jsonData.get("user_pw")
        );

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    // 클라이언트의 요청을 받아서 JSON 문자열을 파싱 -> Map으로 만들어주는 메서드
    private Map<String, String> parseRequestJSON(HttpServletRequest request) {
        try (Reader reader = new InputStreamReader(request.getInputStream())) {
            // JSON 문자열을 DTO 클래스의 데이터로 변경
            Gson gson = new Gson();

            // 속성의 이름이 Key가 되고 값이 Value가 된다.
            return gson.fromJson(reader, Map.class);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
