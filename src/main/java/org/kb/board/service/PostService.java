package org.kb.board.service;

import org.kb.board.domain.PostEntity;
import org.kb.board.domain.UserEntity;
import org.kb.board.dto.PostDto;

public interface PostService {
    // DTO -> Entity, 같은 패키지에서만 접근 가능하도록 default
    default PostEntity dtoToEntity(PostDto postDto) {
        UserEntity userEntity = UserEntity.builder()
                .userId(postDto.getUserId()).build();

        PostEntity postEntity = PostEntity.builder()
                .postId(postDto.getPostId())
                .writer(userEntity)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .build();

        return postEntity;
    }

    default PostDto entityToDto(PostEntity postEntity, UserEntity userEntity, Long replyCnt) {
        PostDto postDto = PostDto.builder()
                .postId(postEntity.getPostId())
                .userId(userEntity.getUserId())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .regDate(postEntity.getRegDate())
                .modDate(postEntity.getModDate())
                .replyCnt(replyCnt.intValue())
                .build();
        return postDto;
    }
}
