package org.kb.board;

import org.junit.jupiter.api.Test;
import org.kb.board.dto.PostDto;
import org.kb.board.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTests {
    @Autowired
    private PostService postService;

    @Test
    public void registerTest() {
        PostDto postDto = PostDto.builder()
                .title("테스트 제목입니다.")
                .content("테스트 내용입니다.")
                .userId(307L)
                .build();
        Long postId = postService.register(postDto);

        System.out.println(postId);
    }
}
