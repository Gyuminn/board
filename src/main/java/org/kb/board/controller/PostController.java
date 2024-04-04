package org.kb.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kb.board.domain.StatusEnum;
import org.kb.board.dto.PageRequestDto;
import org.kb.board.dto.PageResponseDto;
import org.kb.board.dto.PostDto;
import org.kb.board.dto.ResponseDto;
import org.kb.board.service.PostService;
import org.kb.board.util.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@RestController
// @Controlelr + @ResponseBody
@RequiredArgsConstructor
@RequestMapping("/v1")
public class PostController {
    private final PostService postService;
    private final JWTUtil jwtUtil;

    // 글 작성하기
    @PostMapping("/auth/register")
    public ResponseEntity<ResponseDto<Long>> registerPost(@RequestHeader("Authorization") String authorization, @RequestBody PostDto postDto) {
        log.info("PostDto: {}", postDto);

        ResponseDto<Long> dto = new ResponseDto<>();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        String accessToken = authorization.substring(7);
        Map<String, Object> values = jwtUtil.validateToken(accessToken);

        postDto.setEmailId(values.get("user_id").toString());
        Long postId = postService.register(postDto);

        dto.setStatusCode(StatusEnum.OK);
        dto.setMessage("글 작성 성공");
        dto.setData(postId);

        return new ResponseEntity<>(dto, header, HttpStatus.OK);
    }
    // 글 다건 조회
    @GetMapping( { "/posts/list"})
    public ResponseEntity<ResponseDto<PageResponseDto<PostDto, Object[]>>> getList(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "keywords", required = false) String keywords
    ) {
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(page == null ? 1 : (int) page)
                .size(size == null ? 10 : (int) size)
                .type(type)
                .keywords(keywords)
                .build();

        log.info("PageRequestDto: {}", pageRequestDto);

        ResponseDto<PageResponseDto<PostDto, Object[]>> dto = new ResponseDto<>();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        PageResponseDto<PostDto, Object[]> newPageResponseDto = postService.getList(pageRequestDto);
        dto.setStatusCode(StatusEnum.OK);
        dto.setMessage("글 다건 조회 성공");
        dto.setData(newPageResponseDto);

        return new ResponseEntity<>(dto, header, HttpStatus.OK);
    }

    // 글 단건 조회
    @GetMapping("/posts/{postId}")
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
    @PostMapping("/auth/update")
    public ResponseEntity<ResponseDto<Long>> update(
            @RequestHeader("Authorization") String authorization,
            @RequestBody PostDto postDto
    ) {
        log.info("PostDto: {}", postDto);

        ResponseDto<Long> dto = new ResponseDto<>();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        String accessToken = authorization.substring(7);
        Map<String, Object> values = jwtUtil.validateToken(accessToken);

        String loginUserEmailId = values.get("user_id").toString();
        if (loginUserEmailId.equals(postDto.getEmailId())) {
            Long postId = postService.modify(postDto);
            dto.setStatusCode(StatusEnum.OK);
            dto.setMessage("글 수정 성공");
            dto.setData(postId);
        } else {
            return new ResponseEntity<>(dto, header, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(dto, header, HttpStatus.OK);
    }

    // 글 삭제
    @DeleteMapping("/auth/{postId}")
    public ResponseEntity<ResponseDto<Long>> delete(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long postId) {
        log.info("PostId: {}", postId);

        ResponseDto<Long> dto = new ResponseDto<>();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        postService.removeWithReplies(postId);
        dto.setStatusCode(StatusEnum.OK);
        dto.setMessage("글 삭제 성공");
        dto.setData(postId);

        return ResponseEntity.ok().headers(header).body(dto);
    }


}
