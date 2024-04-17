package org.kb.board.repository;

import org.kb.board.domain.PostEntity;
import org.kb.board.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

// 검색을 위한 메서드를 소유할 Repository 인터페이스
public interface SearchPostRepository {
    PostEntity search1();

    // 검색 메서드
    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);

    // 엑셀 다운로드를 위한 전체 검색 메서드
    List<PostDto> searchAll(String type, String keyword);
}
