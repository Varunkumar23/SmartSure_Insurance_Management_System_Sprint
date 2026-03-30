package com.smartsure.email_service.consumer;


import com.smartsure.email_service.dto.EmailMessage;
import com.smartsure.email_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = "policy-email-queue")
    public void consume(EmailMessage message) {

        System.out.println("📩 Received message: " + message);

        emailService.sendEmail(
                message.getEmail(),
                message.getPolicyName()
        );
    }
}
