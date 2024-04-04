package org.kb.board;

import org.junit.jupiter.api.Test;
import org.kb.board.dto.PageRequestDto;
import org.kb.board.dto.PageResponseDto;
import org.kb.board.dto.PostDto;
import org.kb.board.dto.ReplyDto;
import org.kb.board.repository.UserRepository;
import org.kb.board.service.PostService;
import org.kb.board.service.ReplyService;
import org.kb.board.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.List;

@SpringBootTest
public class ServiceTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PostService postService;

    @Autowired
    private ReplyService replyService;

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
        Long postId = 50L;
        PostDto postDto = postService.get(postId);
        System.out.println(postDto);
    }

    // 게시글 삭제하기
    @Test
    public void deletePost() {
        postService.removeWithReplies(99L);
    }

    // 게시글 수정하기
    @Test
    public void updatePost() {
        PostDto postDto = PostDto.builder()
                .postId(50L)
                .title("수정된 제목입니다.")
                .content("수정된 본문입니다.")
                .build();
        Long postId = postService.modify(postDto);

        System.out.println(postId);
    }

    // 글 번호로 댓글 목록 가져오기
    @Test
    public void getReplyList() {
        List<ReplyDto> list = replyService.getList(909L);
        list.forEach(System.out::println);
    }

    // 댓글 삽입 테스트
    @Test
    public void insertReply() {
        ReplyDto replyDto = ReplyDto.builder()
                .content("댓글 삽입 테스트")
                .replyer("테스트 하는 사람")
                .postId(909L)
                .build();
        System.out.println(replyService.register(replyDto));
    }


}