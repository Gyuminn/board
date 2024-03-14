package org.kb.board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {
    private Long userId;

    private String emailId;

    private String password;

    private String nickname;

    private String introContent;

    private String provider;

    @Builder
    public UserDto(Long userId, String emailId, String password, String nickname, String introContent, String provider) {
        this.userId = userId;
        this.emailId = emailId;
        this.password = password;
        this.nickname = nickname;
        this.introContent = introContent;
        this.provider = provider;
    }
}
