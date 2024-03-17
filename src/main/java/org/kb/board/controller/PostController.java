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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
