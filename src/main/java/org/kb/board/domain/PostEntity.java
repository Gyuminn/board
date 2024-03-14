package org.kb.board.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"writer"}) //toString을 만들 때 writer의 ToString은 호출 안함.
public class PostEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // 게시글은 회원을 알 수 있다. 회원은 게시글을 알 수 없다.
    // 없으므로 단방향(객체관계-메서드)이지만, 테이블 관계는 항상 양방향(서로 조인 가능)이다.
    // 그런데 한 사람이 쓴 글들을 보고 싶으니까 @OneToMany를 UserEntity에 추가해보자. -> 그럼 이제 흔히 말하는 양방향이 된다.
    // 연관관계의 주인은 외래 키가 있는 곳이다.
    // Join을 바로 수행하게 되는데 이는 자원의 낭비이므로 필요할 때 불러오자 -> 지연 로딩.
    // 지연로딩을 사용할 때 주의점은 @ToString 이다. 지연로딩을 하게되면 참조하는 속성의 값이 null이기 떄문에 NPL이 발생한다.
    // 또한 게시글이나 댓글을 조회하는 메서드(ex.findById)를 만들게 된다면 이 떄 @Transactional을 붙여줘야 한다. Join이 아니라 지연 로딩으로 인해 두 번의 select를 호출하기 때문이다.
    @JoinColumn(name = "user_id")
    private UserEntity writer;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1500, nullable = false)
    private String content;

    @Builder
    public PostEntity(Long postId, UserEntity writer, String title, String content) {
        this.postId = postId;
        this.writer = writer;
        this.title = title;
        this.content = content;
    }

}
