package org.kb.board.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
// Entity 클래스 생성, 내부적으로 기본 생성자를 만들어줌. NoArgs 필요 x
@Table(name = "user")
// 테이블 이름 설정, 매칭
@Getter
// ORM에서 영속성(데이터베이스의 데이터와 엔티티가 일관된 상태를 유지해야 함)을 위해 Setter는 붙이지 않는다.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 안써도 되지만 안전성을 위해 접근 제한을 위해 붙여준 것. private이 아닌 이유는 JPA선에서 최대한의 접근 레벨이 protected
// @AllArgsConstructor
// @Builder
// ORM에서는 일관성유지 때문에 setter와 new()를 쓰지 않음. 그래서 빌더 패턴을 씀. 그럼 @Builder를 붙여주면 되는데
// 이 때 전체 필드에 대한 값을 요구하고 생성가 반드시 필요하다. 그래서  빌더는 해당 클래스에 생성자가 없다면 만들어주고 있으면 그걸 사용한다.
// 그런데 위에서 NoArgs로 만들어주어서 있다고 판단하기 때문에 새로 생성하지 않는데 이 때 접근에러가 발생한다. 그래서 AllArgs로 전체 필드에 대한
// 생성자를 만들어주면 빌더 패턴을 적용할 수 있다.
// 혹은 AllArgs를 빼고 메서드 별로 @Builder 패턴을 적용시켜도 된다.
@ToString
public class UserEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO -> MySql -> IDENTITY
    @Column(name = "user_id")
    private Long userId;

    @Column(length = 100, nullable = false)
    private String emailId;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 30, nullable = false)
    private String nickname;

    @Column(length = 100)
    private String introContent;

    // 로그인 방법 -> ENUM 타입으로 하면 어떨까? -> @Lob
    @Column(nullable = false)
    private String provider;

    @Builder
    public UserEntity(Long userId, String emailId, String password, String nickname, String introContent, String provider) {
        this.userId = userId;
        this.emailId = emailId;
        this.password = password;
        this.nickname = nickname;
        this.introContent = introContent;
        this.provider = provider;
    }

    // @OneToMany(mappedBy = "writer")
    // 한 사람이 쓴 게시글을 모아보기 -> mappedBy 속성은 양방향 매핑일 때 사용하는데 반대쪽 매핑의 필드 이름을 값으로 주면 된다.
    // 엄밀히 말하면 양방향은 없고, 단방향 2개를 묶어서 그렇게 보이게 한다.
    // mappedBy를 썼으므로 연관관계의 주인은 얘가 아니고 writer이다.
    // 하지만 장점은 객체 그래프 탐색 기능이 추가된 것 뿐이다. 나중에 추가해보자.
    // private List<PostEntity> posts = new ArrayList<PostEntity>();
}
