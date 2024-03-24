package org.kb.board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class SampleController {
    @GetMapping("/")
    public void index() {
        log.info("메인");
    }
}
