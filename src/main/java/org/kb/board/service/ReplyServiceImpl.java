package org.kb.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kb.board.domain.PostEntity;
import org.kb.board.domain.ReplyEntity;
import org.kb.board.dto.ReplyDto;
import org.kb.board.repository.ReplyRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{
    private final ReplyRepository replyRepository;

    // 댓글 등록하기
    @Override
    public Long register(ReplyDto replyDto) {
        ReplyEntity replyEntity = dtoToEntity(replyDto);
        replyRepository.save(replyEntity);
        return replyEntity.getReplyId();
    }

    // 특정 게시물의 댓글 목록 가져오기
    @Override
    public List<ReplyDto> getList(Long postId) {
        List<ReplyEntity> result = replyRepository.findByPostOrderByReplyId(PostEntity.builder()
                .postId(postId).build());

        // result 정렬(등록한 시간 내림차순)
        result.sort(new Comparator<ReplyEntity>() {
            @Override
            public int compare(ReplyEntity o1, ReplyEntity o2) {
                return o2.getRegDate().compareTo(o1.getRegDate());
            }
        });

        return result.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    // 댓글 수정하기
    @Override
    public Long modify(ReplyDto replyDto) {
        if (replyDto.getReplyId() == null) {
            return 0L;
        }

        Optional<ReplyEntity> replyEntity = replyRepository.findById(replyDto.getReplyId());

        if (replyEntity.isPresent()) {
            replyEntity.get().changeContent(replyDto.getContent());
            replyRepository.save(replyEntity.get());
            return replyEntity.get().getReplyId();
        } else {
            return 0L;
        }
    }

    // 댓글 삭제하기
    @Override
    public Long remove(Long replyId) {
        replyRepository.deleteById(replyId);
        return replyId;
    }
}
