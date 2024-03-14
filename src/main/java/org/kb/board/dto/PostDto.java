package org.kb.board.dto;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

// DTO
// 직렬화: Java Object -> JSON 변환(서버 -> 클라이언트로 ResponseBody를 이용할 때)
// 역직렬화: JSON -> Java Object   (클라이언트 -> 서버로 RequestBody를 이용할 때)
// 둘다 Jackson을 사용하는데 내부적으로 ObjectMapper가 자바 리플렉션을 사용하고 이 때 기본 생성자, getter, setter가 필요하다.
// 그런데 필드에 꼭 다 설정하지 않아도 된다.
// 직렬화: 기본생성자 필요 없음, getter 필요, setter 불가능
// 역직렬화: 기본생성자 필요함, getter or setter 중 하나만 있으면 됨. (동작 방식: 기본 생성자로 객체 생성하고 getter나 setter로 필드 가져옴. 근데 getter가 나음.)
public class PostDto {
    private Long postId;

    private String title;

    private String content;

    private String writerNickName;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    private int replyCnt;

}
