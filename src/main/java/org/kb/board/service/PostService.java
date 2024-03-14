package org.kb.board.service;

import org.kb.board.domain.PostEntity;
import org.kb.board.domain.UserEntity;
import org.kb.board.dto.PostDto;
import org.kb.board.dto.UserDto;

public interface PostService {
    // 게시글 등록
    Long register(PostDto postDto);
}
