package com.smartsure.policy_service.producer;

import com.smartsure.policy_service.dto.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PolicyEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendPolicyPurchaseEvent(String email, String policyName) {

        EmailMessage message = new EmailMessage();
        message.setEmail(email);
        message.setPolicyName(policyName);

        rabbitTemplate.convertAndSend(
                "policy-exchange",   // must match
                "policy.email",      // must match
                message
        );

        System.out.println("✅ Message sent to RabbitMQ");
    }
}
