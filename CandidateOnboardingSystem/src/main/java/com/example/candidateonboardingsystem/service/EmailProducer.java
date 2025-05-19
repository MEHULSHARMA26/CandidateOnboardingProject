package com.example.candidateonboardingsystem.service;

import com.example.candidateonboardingsystem.config.RabbitMQConfig;
import com.example.candidateonboardingsystem.dto.EmailMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailProducer {

    private final RabbitTemplate rabbitTemplate;

    public EmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEmailMessage(EmailMessage message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_QUEUE, message);
    }
}
