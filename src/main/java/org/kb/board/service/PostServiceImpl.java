package org.kb.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kb.board.domain.PostEntity;
import org.kb.board.domain.ReplyEntity;
import org.kb.board.domain.UserEntity;
import org.kb.board.dto.PageRequestDto;
import org.kb.board.dto.PageResponseDto;
import org.kb.board.dto.PostDto;
import org.kb.board.dto.UserDto;
import org.kb.board.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
// 빈 등록, 명시해주기 위함.
@RequiredArgsConstructor
// @Autowired를 사용하지 않고 의존성 주입.
// 의존성 주입이란 클래스간의 결합도를 유연하게 하고 외부로부터 주입받아 사용한다.
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;


    // 게시글 등록하기
    public Long register(PostDto postDto) {
        log.info("Service - register: {}", postDto);
        UserEntity userEntity = UserEntity.builder()
                .userId(postDto.getUserId())
                .build();

        PostEntity postEntity = PostEntity.builder()
                .writer(userEntity)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .build();

        postRepository.save(postEntity);
        return postEntity.getPostId();
    }

    // 게시글 목록 보기
    public PageResponseDto<PostDto, Object[]> getList(PageRequestDto pageRequestDto) {
        log.info("PageRequestDto : {}", pageRequestDto);

        // Entity를 Dto로 변경하는 람다 인스턴스 생성
        Function<Object[], PostDto> fn = (en -> entityToDTO((PostEntity) en[0], (UserEntity) en[1], (Long) en[2]));

        // 페이지 목록 보기 요청
        Page<Object[]> result = postRepository
                .getPostEntityWithWriterAndReplyCount(
                        pageRequestDto.getPageable(Sort.by("postId").descending())
                );

        return new PageResponseDto<>(result, fn);
    }

    // 게시글 상세보기
    public PostDto get(Long postId) {
        List<Object[]> result = postRepository.getPostEntityByPostId(postId);

        return entityToDTO((PostEntity) result.get(0)[0], (UserEntity) result.get(0)[1], (Long) result.get(0)[2]);
    }
}
