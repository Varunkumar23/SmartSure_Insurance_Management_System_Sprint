package com.smartsure.email_service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String QUEUE = "policy-email-queue";
    public static final String EXCHANGE = "policy-exchange";
    public static final String ROUTING_KEY = "policy.email";

    // Queue
    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true); // durable
    }

    // Exchange
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    // Binding
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_KEY);
    }
}