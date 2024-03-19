package org.kb.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ReplyDto {
    private Long replyId;
    private Long postId;

    private String replyer;

    private String content;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    @Builder
    public ReplyDto(Long replyId, Long postId, String replyer, String content, LocalDateTime regDate, LocalDateTime modDate) {
        this.replyId = replyId;
        this.postId = postId;
        this.replyer = replyer;
        this.content = content;
        this.regDate = regDate;
        this.modDate = modDate;
    }
}
