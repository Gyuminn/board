package org.kb.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.kb.board.domain.*;
import org.kb.board.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.ArrayList;
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

    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        QPostEntity postEntity = QPostEntity.postEntity;
        QUserEntity userEntity = QUserEntity.userEntity;
        QReplyEntity replyEntity = QReplyEntity.replyEntity;

        JPQLQuery<PostEntity> jpqlQuery = from(postEntity);
        jpqlQuery.leftJoin(userEntity).on(postEntity.writer.eq(userEntity));
        jpqlQuery.leftJoin(replyEntity).on(replyEntity.post.eq(postEntity));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(postEntity, userEntity, replyEntity.count());

        // 조건 생성
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = postEntity.postId.gt(0L); // postId가 0보다 큰
        booleanBuilder.and(expression);

        // 타입에 따른 조건 생성
        // 제목 검색 - t
        // 작성자 검색 - w
        // 내용 검색 - c
        // 재목 + 내용 검색: tc
        // 제목 + 작성자 검색: tw
        if (type != null) {
            // 글자 단위로 쪼객
            String[] typeArr = type.split("");
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            for (String t : typeArr) {
                switch(t) {
                    case "t":
                        conditionBuilder.or(postEntity.title.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(postEntity.content.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(userEntity.emailId.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }

        // 조건을 tuple에 적용
        tuple.where(booleanBuilder);

        // 정렬 방법 설정
        tuple.orderBy(postEntity.postId.desc());

        // 그룹화
        tuple.groupBy(postEntity);

        // page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        // 데이터 가져오기
        List<Tuple> result = tuple.fetch();

        List<Object[]> resultList = new ArrayList<>();
        for (Tuple t : result) {
            resultList.add(t.toArray());
        }
        return new PageImpl<Object[]>(resultList, pageable, tuple.fetchCount());
    }

    public List<PostDto> searchAll(String type, String keyword) {
        QPostEntity postEntity = QPostEntity.postEntity;
        QUserEntity userEntity = QUserEntity.userEntity;

        JPQLQuery<PostEntity> jpqlQuery = from(postEntity);
        jpqlQuery.leftJoin(userEntity).on(postEntity.writer.eq(userEntity));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(postEntity, userEntity);

        // 조건 생성
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = postEntity.postId.gt(0L); // postId가 0보다 큰
        booleanBuilder.and(expression);

        // 타입에 따른 조건 생성
        // 제목 검색 - t
        // 작성자 검색 - w
        // 내용 검색 - c
        // 재목 + 내용 검색: tc
        // 제목 + 작성자 검색: tw
        if (type != null) {
            // 글자 단위로 쪼객
            String[] typeArr = type.split("");
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            for (String t : typeArr) {
                switch(t) {
                    case "t":
                        conditionBuilder.or(postEntity.title.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(postEntity.content.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(userEntity.emailId.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }

        // 조건을 tuple에 적용
        tuple.where(booleanBuilder);

        // 정렬 방법 설정
        tuple.orderBy(postEntity.postId.desc());

        // 그룹화
        tuple.groupBy(postEntity);

        // 데이터 가져오기
        List<Tuple> result = tuple.fetch();

        List<Object[]> resultList = new ArrayList<>();
        for (Tuple t : result) {
            resultList.add(t.toArray());
        }

        List<PostDto> postDtoList = new ArrayList<>();
        for (Tuple t : result) {
            Object[] row = t.toArray();
            PostEntity postEntity1 = (PostEntity) row[0];
            UserEntity userEntity1 = (UserEntity) row[1];

            PostDto postDto = PostDto.builder()
                    .postId(postEntity1.getPostId())
                    .title(postEntity1.getTitle())
                    .content(postEntity1.getContent())
                    .regDate(postEntity1.getRegDate())
                    .modDate(postEntity1.getModDate())
                    .emailId(userEntity1.getEmailId())
                    .build();

            postDtoList.add(postDto);
        }

        return postDtoList;
    }
}
