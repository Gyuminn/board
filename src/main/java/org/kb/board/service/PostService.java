package org.kb.board.service;

import org.kb.board.domain.PostEntity;
import org.kb.board.domain.UserEntity;
import org.kb.board.dto.PageRequestDto;
import org.kb.board.dto.PageResponseDto;
import org.kb.board.dto.PostDto;
import org.kb.board.dto.UserDto;

public interface PostService {
    // DTO -> Entity로 변환해주는 메서드
    default PostEntity dtoToEntity(PostDto dto) {
        UserEntity userEntity = UserEntity.builder()
                .userId(dto.getUserId()).build();

        PostEntity postEntity = PostEntity.builder()
                .writer(userEntity)
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
        return postEntity;
    }

    // Entity -> DTO로 변환해주는 메서드
    default PostDto entityToDTO(PostEntity postEntity, UserEntity userEntity, Long replyCnt) {
        PostDto dto = PostDto.builder()
                .postId(postEntity.getPostId())
                .userId(userEntity.getUserId())
                .emailId(userEntity.getEmailId())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .regDate(postEntity.getRegDate())
                .modDate(postEntity.getModDate())
                .replyCnt(replyCnt.intValue())
                .build();
        return dto;
    }
    // 게시글 등록
    Long register(PostDto postDto);

    // 게시글 목록 출력하기
    PageResponseDto<PostDto, Object[]> getList(PageRequestDto pageRequestDto);

    // 게시글 상세보기
    PostDto get(Long postId);

    // 게시글 삭제하기
    void removeWithReplies(Long postId);

    // 게시글 수정하기
    Long modify(PostDto postDto);
}
