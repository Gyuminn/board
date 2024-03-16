package org.kb.board.repository;

import org.kb.board.domain.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {
    // 게시물을 삭제하고 싶을 때 자식 Entity인 댓글 삭제는 어떻게 할 것인지 고민해야 한다.
    @Modifying
    // @Modifying은 @Query 어노테이션(JPQL Query, Native Query)을 통해 작성된 INSERT, UPDATE, DELETE (SELECT 제외) 쿼리에서 사용되는 어노테이션이다.
    // 기본적으로 JpaRepository에서 제공하는 메서드 혹은 메서드 네이밍으로 만들어진 쿼리에는 적용되지 않는다.
    //clearAutomatically, flushAutomatically 속성을 변경할 수 있으며 주로 벌크 연산과 같이 이용된다.
    // JPA Entity Life-cycle을 무시하고 쿼리가 실행되기 때문에 해당 어노테이션을 사용할 때는 영속성 컨텍스트 관리에 주의해야 한다. (clearAutomatically, flushAutomatically를 통해 간단하게 해결할 수 있다.)
    @Query("delete from ReplyEntity r where r.post.postId = :postId")
    void deleteByPostId(@Param("postId") Long postId);
}
