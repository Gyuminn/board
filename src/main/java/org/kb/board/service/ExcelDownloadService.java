package org.kb.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.kb.board.dto.PostDto;
import org.kb.board.repository.PostRepository;
import org.kb.board.repository.SearchPostRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelDownloadService {
    private final PostRepository postRepository;

    public byte[] downloadPostsAsExcel(String type, String keywords) {


        List<PostDto> posts = postRepository.searchAll(type, keywords);

        // 엑셀 워크북 생성
        try (Workbook workbook = new XSSFWorkbook()) {
            // 시트 생성
            Sheet sheet = workbook.createSheet("Posts");

            // 헤더 행 생성
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Post ID", "User Email", "Title", "Content", "Reg Date", "Mod Date"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                sheet.setColumnWidth(i, 50 * 256); // 각 열을 50 너비로 설정
            }

            // 날짜 형식 지정
            CellStyle dateStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));

            // 데이터 행 생성
            int rowNum = 1;
            for (PostDto postDto : posts) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(postDto.getPostId());
                row.createCell(1).setCellValue(postDto.getEmailId());
                row.createCell(2).setCellValue(postDto.getTitle());
                row.createCell(3).setCellValue(postDto.getContent());

                Cell regDateCell = row.createCell(4);
                regDateCell.setCellValue(postDto.getRegDate());
                regDateCell.setCellStyle(dateStyle);

                Cell modDateCell = row.createCell(5);
                modDateCell.setCellValue(postDto.getModDate());
                modDateCell.setCellStyle(dateStyle);

            }

            // 파일 저장
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            log.info("Excel workbook 생성 성공");
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("Excel workbook 생성 에러: ", e.getMessage());
            return null;
        }
    }
}
