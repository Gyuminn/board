package org.kb.board.security.filter;


import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.kb.board.security.RefreshTokenException;
import org.kb.board.util.JWTUtil;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
//OncePerRequestFilter 는 요청이 올 때 마다 생성되는 필터
public class RefreshTokenFilter extends OncePerRequestFilter {
    private final String refreshPath;
    private final JWTUtil jwtUtil;
    //눈에 보이지 않지만 생성자가 만들어 진다.
    /*
    RefreshTokenFilter(String refreshPath, JWTUtil jwtUtil){
        super();
        this.refreshPath = refreshPath;
        this.jwtUtil = jwtUtil;
   }
     */

    //필터로 동작하는 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        //클라이언트의 요청 URL
        String path = request.getRequestURI();
        //URL이 다르면 다음 필터로 진행
        if(!path.equals(refreshPath)){
            log.info("RefreshTokenFilter 넘어감");
            //다음 필터로 진행
            filterChain.doFilter(request, response);
            //필터의 동작을 중지시킬 때는 return을 해주어야 한다.
            return;
        }
        log.info("refresh Token 필터 동작");

        Map<String, String> tokens = parseRequestJSON(request);
        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        log.info("accessToken:" + accessToken);
        log.info("refreshToken:" + refreshToken);

        //토큰의 유효성 검사
        try{
            checkRefreshToken(accessToken);
        }catch(RefreshTokenException refreshTokenException){
            refreshTokenException.sendResponseError(response);
            return;
        }

        Map<String, Object> refreshClaims = null;
        try{
            refreshClaims = checkRefreshToken(refreshToken);
            log.info(refreshClaims);
        }catch(RefreshTokenException refreshTokenException){
            refreshTokenException.sendResponseError(response);
            return;
        }

        //토큰을 생성해서 전송
        //refreshToken의 만료 시간을 확인
        Integer exp = (Integer)refreshClaims.get("exp");
        //토큰 만료 시간
        Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);
        //현재 시간
        Date current = new Date(System.currentTimeMillis());

        //시간 차이
        long gapTime = (expTime.getTime() - current.getTime());

        //출력
        log.info("현재 시간:" + current);
        log.info("만료 시간:" + expTime);
        log.info("남은 시간:" + gapTime);

        //토큰을 만들 때 사용한 id 찾아오기
        String userId = (String) refreshClaims.get("user_id");

        //accessToken은 무조건 재발급
        String accessTokenValue = jwtUtil.generateToken(Map.of("user_id", userId), 1);

        //refreshToken은 3분이 남지 않은 경우만 재발급
        String refreshTokenValue = tokens.get("refreshToken");
        if(gapTime < (1000 * 60 * 3)){
            refreshTokenValue = jwtUtil.generateToken(Map.of("user_id", userId), 30);
        }
        //토큰 전송
        sendTokens(accessTokenValue, refreshTokenValue, response);
    }


    //클라이언트의 요청으로 부터 파라미터를 Map으로 변환하는 메서드
    private Map<String, String> parseRequestJSON(HttpServletRequest request){
        try(Reader reader = new InputStreamReader(request.getInputStream())){
            Gson gson = new Gson();
            return gson.fromJson(reader, Map.class);
        }catch(Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

    //accessToken을 검증하는 메서드
    private void checkAccessToken(String accessToken) throws RefreshTokenException {
        try{
            jwtUtil.validateToken(accessToken);
        }catch(ExpiredJwtException expiredJwtException){
            log.info("유효 시간 만료");
        }catch(Exception exception){
            throw new RefreshTokenException(
                    RefreshTokenException.ErrorCase.NO_ACCESS);
        }
    }

    //RefreshToken 검증
    private Map<String, Object> checkRefreshToken(String refreshToken)
            throws RefreshTokenException{
        try{
            Map<String, Object> values = jwtUtil.validateToken(refreshToken);
            return values;
        }catch(ExpiredJwtException exception){
            throw new RefreshTokenException(
                    RefreshTokenException.ErrorCase.OLD_REFRESH);
        }catch(MalformedJwtException malformedJwtException){
            throw new RefreshTokenException(
                    RefreshTokenException.ErrorCase.NO_REFRESH);
        }catch(Exception exception){
            throw new RefreshTokenException(
                    RefreshTokenException.ErrorCase.NO_REFRESH);
        }
    }

    //토큰을 전송하는 메서드
    private void sendTokens(String accessTokenValue, String refreshTokenValue,
                            HttpServletResponse response){
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Gson gson = new Gson();

        String jsonStr = gson.toJson(Map.of("accessToken", accessTokenValue,
                "refreshToken", refreshTokenValue));
        try{
            response.getWriter().println(jsonStr);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

}