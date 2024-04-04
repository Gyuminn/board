package org.kb.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.kb.board.domain.StatusEnum;
import org.kb.board.dto.ReplyDto;
import org.kb.board.dto.ResponseDto;
import org.kb.board.service.ReplyService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/replies")
public class ReplyController {
    private final ReplyService replyService;

    // 댓글 작성하기
    @PostMapping("")
    public ResponseEntity<ResponseDto<Long>> register(@RequestBody ReplyDto replyDto) {

        log.info("ReplyDto: {}", replyDto);

        ResponseDto<Long> dto = new ResponseDto<>();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        Long postId = replyService.register(replyDto);

        dto.setStatusCode(StatusEnum.OK);
        dto.setMessage("댓글 작성 성공");
        dto.setData(postId);

        return new ResponseEntity<>(dto, header, HttpStatus.OK);

    }

    // 특정 게시글의 댓글들을 조회
    @GetMapping("/post/{postId}")
    public ResponseEntity<ResponseDto<List<ReplyDto>>> getByPost(@PathVariable("postId") Long postId) {
        log.info("postId: {}", postId);

        ResponseDto<List<ReplyDto>> dto = new ResponseDto<>();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        List<ReplyDto> newListReplyDto = replyService.getList(postId);

        dto.setStatusCode(StatusEnum.OK);
        dto.setMessage("댓글 리스트 조회 성공");
        dto.setData(newListReplyDto);

        return new ResponseEntity<>(dto, header, HttpStatus.OK);

    }

    // 댓글 수정
    @PatchMapping("/update")
    public ResponseEntity<ResponseDto<Long>> update(@RequestBody ReplyDto replyDto) {
        log.info("ReplyDto: {}", replyDto);

        ResponseDto<Long> dto = new ResponseDto<>();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        Long postId = replyService.modify(replyDto);

        dto.setStatusCode(StatusEnum.OK);
        dto.setMessage("댓글 수정 성공");
        dto.setData(postId);

        return new ResponseEntity<>(dto, header, HttpStatus.OK);

    }

    // 댓글 삭제
    @DeleteMapping("/{replyId}")
    public ResponseEntity<ResponseDto<Long>> remove(@PathVariable("replyId") Long replyId) {
        log.info("ReplyId: {}", replyId);

        ResponseDto<Long> dto = new ResponseDto<>();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        Long postId = replyService.remove(replyId);

        dto.setStatusCode(StatusEnum.OK);
        dto.setMessage("댓글 삭제 성공");
        dto.setData(postId);

        return new ResponseEntity<>(dto, header, HttpStatus.OK);
    }
}
