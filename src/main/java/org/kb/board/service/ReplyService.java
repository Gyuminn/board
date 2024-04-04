package org.kb.board.service;

import org.kb.board.domain.PostEntity;
import org.kb.board.domain.ReplyEntity;
import org.kb.board.dto.ReplyDto;

import java.util.List;

public interface ReplyService {
    default ReplyEntity dtoToEntity(ReplyDto dto) {
        PostEntity postEntity = PostEntity.builder()
                .postId(dto.getPostId()).build();

        return ReplyEntity.builder()
                .content(dto.getContent())
                .replyer(dto.getReplyer())
                .post(postEntity)
                .build();
    }

    default ReplyDto entityToDto(ReplyEntity replyEntity) {
        ReplyDto dto = ReplyDto.builder()
                .replyId(replyEntity.getReplyId())
                .postId(replyEntity.getPost().getPostId())
                .content(replyEntity.getContent())
                .replyer(replyEntity.getReplyer())
                .regDate(replyEntity.getRegDate())
                .modDate(replyEntity.getModDate())
                .build();

        return dto;
    }

    // 댓글 등록
    Long register(ReplyDto replyDto);

    // 댓글 목록
    List<ReplyDto> getList(Long postId);

    // 댓글 수정
    Long modify(ReplyDto replyDto);

    // 댓글 삭제
    Long remove(Long replyId);
}
