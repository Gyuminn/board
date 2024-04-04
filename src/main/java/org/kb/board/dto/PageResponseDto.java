package org.kb.board.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@ToString
public class PageResponseDto<Dto, En> {
    // 페이지 목록
    private List<Dto> dtoList;

    // 전체 페이지 개수
    private int totalPage;

    // 현재 페이지
    private int page;

    // 페이지 당 데이터 출력 개수
    private int size;

    // 페이지의 시작 번호와 끝 번호
    private int startPage;
    private int endPage;

    // 이전과 다음 출력 여부
    private boolean prev;
    private boolean next;

    // 페이지 번호 목록
    private List<Integer> pageList;

    // 검색 결과(Page<PostEntity>)를 가지고 데이터를 생성해주는 메서드
    public PageResponseDto(Page<En> result, Function<En, Dto> fn) {
        // 검색 결과 목록을 Dto의 List로 변환
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        // 전체 페이지 구하기
        totalPage = result.getTotalPages();

        // 페이지 번호 목록 관련 필드을 결정하는 메서드
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {
        // 현제 페이지 번호
        this.page = pageable.getPageNumber() + 1;

        // 데이터 개수
        this.size = pageable.getPageSize();

        // 임시로 마지막 페이지 계산
        int tempEnd = (int) (Math.ceil(page / 10.0)) * 10;

        startPage = tempEnd - 9;
        prev = startPage > 1;

        endPage = totalPage > tempEnd ? tempEnd : totalPage;

        next = totalPage > endPage;

        pageList = IntStream.rangeClosed(startPage, endPage).boxed().collect(Collectors.toList());

    }
}
