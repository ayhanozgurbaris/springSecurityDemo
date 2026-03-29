package com.security.controller;

import com.security.service.RabbitMQProducerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
public class RabbitMQController {

    private final RabbitMQProducerService producerService;

    public RabbitMQController(RabbitMQProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping("/send")
    public String sendMessage(@RequestParam("msg") String msg) {
        producerService.sendMessageToQueue(msg);
        return "Tebrikler! Mesaj kuyruğa başarıyla fırlatıldı: " + msg;
    }



}
