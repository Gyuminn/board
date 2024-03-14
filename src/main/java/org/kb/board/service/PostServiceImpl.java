package org.kb.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kb.board.domain.PostEntity;
import org.kb.board.domain.UserEntity;
import org.kb.board.dto.PostDto;
import org.kb.board.dto.UserDto;
import org.kb.board.repository.PostRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
// 빈 등록, 명시해주기 위함.
@RequiredArgsConstructor
// @Autowired를 사용하지 않고 의존성 주입.
// 의존성 주입이란 클래스간의 결합도를 유연하게 하고 외부로부터 주입받아 사용한다.
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;


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
}
