package com.example.candidateonboardingsystem.service;

import com.example.candidateonboardingsystem.config.RabbitMQConfig;
import com.example.candidateonboardingsystem.dto.EmailMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

    private final JavaMailSender mailSender;

    public EmailConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void receiveMessage(EmailMessage message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(message.getTo());
        mail.setSubject(message.getSubject());
        mail.setText(message.getBody());
        mailSender.send(mail);
        System.out.println("Email sent to: " + message.getTo());
    }
}

