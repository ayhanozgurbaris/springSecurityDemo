package com.security.controller;

import com.security.service.RedisTestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/redis")
public class RedisTestController {

    private final RedisTestService redisTestService;

    public RedisTestController(RedisTestService redisTestService) {
        this.redisTestService = redisTestService;
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable String id) {
        return redisTestService.getProductById(id);
    }

}
