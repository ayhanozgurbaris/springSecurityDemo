package com.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/testLog")
    public String test() {
        log.info("Bu bir INFO logudur ve DB'ye yazılır.");
        log.error("Bu bir ERROR logudur ve detaylarıyla DB'ye yazılır.");
        return "Loglar gönderildi!";
    }
}