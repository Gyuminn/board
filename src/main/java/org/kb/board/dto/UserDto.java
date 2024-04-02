package org.kb.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@ToString
public class UserDto extends User {
    private Long userId;

    private String emailId;

    private String password;

    private String nickname;

    private String introContent;

    private String provider;

    // security의 User를 상속받으면 super로 세가지를 전달해줘야 함.
    public UserDto(Long userId, String emailId, String password, String nickname, String introContent, String provider, Collection<GrantedAuthority> authorities) {
        // Spring Security는 아이디, 비번, 권한의 모임이 기본 정보
        super(emailId, password, authorities);
        this.userId = userId;
        this.emailId = emailId;
        this.password = password;
        this.nickname = nickname;
        this.introContent = introContent;
        this.provider = provider;
    }
}
