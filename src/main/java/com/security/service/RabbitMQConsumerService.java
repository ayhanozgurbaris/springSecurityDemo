package com.security.service;

import com.security.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumerService {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consumeMessage(String message) {

        System.out.println("==================================================");
        System.out.println(" CONSUMER MESAJI YAKALADI: " + message);
        System.out.println("==================================================");

    }
}
