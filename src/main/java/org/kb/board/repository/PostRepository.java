package org.kb.board.repository;

import org.kb.board.domain.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long>, SearchPostRepository{
    // Post 가져올 때 Writer 정보도 같이 가져오기
    // Spring Data Jpa 쿼리 메소드 기능
    // 영속성 컨텍스트를 거쳐 쓰기지연 SQL이 아니라 바로 데이터베이스에 질의한다.
    // @Query(”select 찾을것 from 엔티티이름 left outer join 엔티티안의참조속성 참조하는테이블의별칭”)
    // 결과가 한 건 이상이면 컬렉션 인터페이스를 사용하고 단건이면 반환 타입을 지정한다.
    @Query("select p, w from PostEntity p left join p.writer w where p.postId=:postId")
    List<Object[]> getPostEntityWithWriter(@Param("postId") Long postId);

    // Post 가져올 때 Reply 정보도 같이 가져오기
    // Post 1개에 여러 개의 Reply가 존재
    // Board와 Reply를 결합한 형태의 목록으로 리턴하기 때문에 List로 쓴다
    // 주의점: Reply 쪽에서 ManyToOne을 작성했기 때문에 PostEntity쪽에서는 Reply에 적어준게 없다. -> on을 통해 join가능
    @Query("select p, r from PostEntity p left join ReplyEntity r on r.post = p where p.postId=:postId")
    List<Object[]> getPostEntityWithReply(@Param("postId") Long postId);

    // Post 목록 가져오기
    // 게시글 목록과 작성자 정보, 그리고 댓글의 개수를 가져오는 메서드
    // 페이징 처리 필요 - 리턴 타입은 Page이다.
    @Query("select p, w, count(r) from PostEntity p left join p.writer w left join ReplyEntity r on r.post = p group by p")
    Page<Object[]> getPostEntityWithWriterAndReplyCount(Pageable pageable);

    // Post 상세 보기
    @Query("select p, w, count(r) from PostEntity p left join p.writer w left join ReplyEntity r on r.post = p where p.postId=:postId")
    List<Object[]> getPostEntityByPostId(@Param("postId") Long postId);


}
