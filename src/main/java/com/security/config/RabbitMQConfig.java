package com.security.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "my_test_queue";

    @Bean
    public Queue myQueue() {
        // true parametresi: "Docker kapansa bile bu kuyruk silinmesin, kalıcı olsun" demektir.
        return new Queue(QUEUE_NAME, true);
    }
}
