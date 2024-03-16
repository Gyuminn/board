package org.kb.board;

import org.junit.jupiter.api.Test;
import org.kb.board.dto.PageRequestDto;
import org.kb.board.dto.PageResponseDto;
import org.kb.board.dto.PostDto;
import org.kb.board.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTests {
    @Autowired
    private PostService postService;

    // 게시글 등록 테스트
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

    // 게시글 목록보기 테스트
    @Test
    public void getPageListTest() {
        PageRequestDto pageRequestDto = new PageRequestDto();
        PageResponseDto<PostDto, Object[]> result = postService.getList(pageRequestDto);

        System.out.println(result.toString());
    }

    // 게시글 상세보기
    @Test
    public void getPost() {
        Long postId = 100L;
        PostDto postDto = postService.get(postId);
        System.out.println(postDto);
    }

    // 게시글 삭제하기
    @Test
    public void deletePost() {
        postService.removeWithReplies(99L);
    }
}