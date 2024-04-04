package org.kb.board;

import org.junit.jupiter.api.Test;
import org.kb.board.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class JWTTests {
    @Autowired
    private JWTUtil jwtUtil;

    @Test
    // 생성확인
    public void testGenerate() {
        Map<String, Object> claimMap = Map.of("user_id", "ABCDE");
        String jwtStr = jwtUtil.generateToken(claimMap, 2); // 분으로 설정함.
        System.out.println(jwtStr);
    }

    // 유효한 토큰인지 확인
    @Test
    public void testValidate() {
        String jwtStr = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTIxNTgxNzEsInVzZXJfaWQiOiJBQkNERSIsImlhdCI6MTcxMjE1ODA1MX0.nGx-YDK9GubOVg6C_Hg1GWg1WxZZ6T9yjouAQzWZP2Q";
        Map<String, Object> claim = jwtUtil.validateToken(jwtStr);
        System.out.println(claim);
    }
}
