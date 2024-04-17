package org.kb.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kb.board.service.ExcelDownloadService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2")
public class ExcelController {
    private final ExcelDownloadService excelDownloadService;

    // 엑셀 다운로드
    @GetMapping({"/download/all"})
    public ResponseEntity<byte[]> downloadPostsAsExcel(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "keywords", required = false) String keywords
    ) {

        byte[] excelContent = excelDownloadService.downloadPostsAsExcel(type, keywords);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        header.setContentDispositionFormData("attachment", "posts.xlsx");

        return new ResponseEntity<>(excelContent, header, HttpStatus.OK);
    }
}
