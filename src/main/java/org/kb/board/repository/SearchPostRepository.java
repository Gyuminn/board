package org.kb.board.repository;

import org.kb.board.domain.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// 검색을 위한 메서드를 소유할 Repository 인터페이스
public interface SearchPostRepository {
    PostEntity search1();

    // 검색 메서드
    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}
