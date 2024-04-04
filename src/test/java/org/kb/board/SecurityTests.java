package org.kb.board;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class SecurityTests {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testEncode() {
        String pw = "1111";
        String enPw = passwordEncoder.encode(pw);

        System.out.println("first encode, enPw: " + enPw);
        enPw = passwordEncoder.encode(pw);
        System.out.println("seconde encode, enPw: " + enPw);
        // 해싱 기법을 달리해서 enPw가 다르게 나온다.
        // 그렇지만 matches로 비교하면 같은지 다른지 알 수 있다.

        boolean result = passwordEncoder.matches(pw, enPw);
        System.out.println("result: " + result); // true
    }
}
