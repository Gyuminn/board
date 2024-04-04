package org.kb.board.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reply")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"post"})
public class ReplyEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long replyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column(nullable = false)
    private String replyer;

    @Column(length = 1000, nullable = false)
    private String content;

    @Builder
    public ReplyEntity(Long replyId, PostEntity post, String replyer, String content) {
        this.replyId = replyId;
        this.post = post;
        this.replyer = replyer;
        this.content = content;
    }

    public void changeContent(String content) {
        if (content == null || content.trim().length() == 0) {
            this.content = "없음";
            return;
        }
        this.content = content;
    }
}
