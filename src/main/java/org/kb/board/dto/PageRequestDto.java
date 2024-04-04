package org.kb.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@ToString
public class PageRequestDto {
    // 페이징 처리를 위한 속성
    private int page;
    private int size;

    // 검색 관련 속성
    private String type;
    private String keywords;

    // 기본값 세팅
    public PageRequestDto() {
        this.page = 1;
        this.size = 10;
    }

    @Builder
    public PageRequestDto(int page, int size, String type, String keywords) {
        this.page = page ;
        this.size = size;
        this.type = type;
        this.keywords = keywords;
    }

    // page와 size를 가지고 Pageable 객체를 생성해주는 메서드
    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }
}
