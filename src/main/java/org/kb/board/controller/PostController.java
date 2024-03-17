package org.kb.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kb.board.domain.StatusEnum;
import org.kb.board.dto.PostDto;
import org.kb.board.dto.ResponseDto;
import org.kb.board.service.PostService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;

@Slf4j
@RestController
// @Controlelr + @ResponseBody
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    // 글 작성하기
    @PostMapping("")
    public ResponseEntity<ResponseDto<Long>> registerPost(@RequestBody PostDto postDto) {
        log.info("PostDto: {}", postDto);

        ResponseDto<Long> dto = new ResponseDto<>();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Long postId = postService.register(postDto);

        dto.setStatusCode(StatusEnum.OK);
        dto.setMessage("글 작성 성공");
        dto.setData(postId);

        return new ResponseEntity<>(dto, header, HttpStatus.OK);
    }

    // 글 단건 조회
    @GetMapping("/{postId}")
    public ResponseEntity<ResponseDto<PostDto>> get(
            @PathVariable Long postId
    ) {
        log.info("PostId: {}", postId);

        ResponseDto<PostDto> dto = new ResponseDto<>();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        PostDto newPostDto = postService.get(postId);
        dto.setStatusCode(StatusEnum.OK);
        dto.setMessage("글 단건 조회 성공");
        dto.setData(newPostDto);

        return new ResponseEntity<>(dto, header, HttpStatus.OK);
    }

    // 글 수정
    @PostMapping("/update")
    public ResponseEntity<ResponseDto<Long>> update(
            @RequestBody PostDto postDto
    ) {
        log.info("PostDto: {}", postDto);

        ResponseDto<Long> dto = new ResponseDto<>();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Long postId = postService.modify(postDto);
        dto.setStatusCode(StatusEnum.OK);
        dto.setMessage("글 수정 성공");
        dto.setData(postId);

        return new ResponseEntity<>(dto, header, HttpStatus.OK);
    }


}
