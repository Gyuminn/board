package org.kb.board.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import org.kb.board.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

// QueryDSL은 Hibernate Query Language의 쿼리를 타입에 안전하게 생성 및 관리해주는 Java 프레임워크이다.
// QueryDSL은 정적 타입을 이용하여 SQL과 같은 쿼리를 생성할 수 있게 해준다.
// JPQL을 문자열로 작성하지 않고 Java 코드로 작성
// 문자열을 사용하지 않으므로 컴파일할 때 오류를 확인하는 것이 가능하다.
public class SearchPostRepositoryImpl extends QuerydslRepositorySupport implements SearchPostRepository {
    // QuerydslRepositorySupport 클래스에 기본 생성자가 없기 때문에
    // 직접 생성해서 호출해줘야 한다. 그렇지 않으면 에러
    // 검색에 사용할 Entity 클래스를 대입해주어야 한다.
    public SearchPostRepositoryImpl() {
        super(PostEntity.class);
    }

    @Override
    public PostEntity search1() {
        // JPQL을 동적으로 생성해서 실행
        QUserEntity userEntity = QUserEntity.userEntity;
        QPostEntity postEntity = QPostEntity.postEntity;
        QReplyEntity replyEntity = QReplyEntity.replyEntity;

        // 쿼리 작성
        JPQLQuery<PostEntity> jpqlQuery = from(postEntity);
        // 왜래키가 writer이다.
        jpqlQuery.leftJoin(userEntity).on(postEntity.writer.eq(userEntity));
        // reply의 외래키는 post이다.
        jpqlQuery.leftJoin(replyEntity).on(replyEntity.post.eq(postEntity));

        /*(
        // 게시글 번호 별로 묶어서 postEntity와 userEntity의 emailId, 그리고 reply의 개수를 가져오기
        jpqlQuery.select(postEntity, userEntity.emailId, replyEntity.count()).groupBy(postEntity);
        // jpql을 실행시킨 결과 가져오기
        // 실행 자체는 fetch가 하는 것이다.
        List<PostEntity> result = jpqlQuery.fetch();
         */
        // select 절에 조회 대상을 지정하는 것을 프로젝션이라 한다.
        // 프로젝션 대상으로 여러 필드를 선택하면 QueryDsl은 기본적으로 com.mysema.query.Tuple이라는 Map과 비슷한 내부 타입을 사용한다.
        // Java에서는 Tuple이라는 자료형이 제공되지 않지만 Spring 에서 제공을 한다.
        JPQLQuery<Tuple> tuple = jpqlQuery.select(postEntity, userEntity.emailId, replyEntity.count());
        tuple.groupBy(postEntity);
        List<Tuple> result = tuple.fetch();

        System.out.println(result);

        return null;
    }
}
