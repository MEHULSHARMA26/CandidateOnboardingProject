package com.example.candidateonboardingsystem;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class CandidateOnboardingSystemApplication {

    public static void main(String[] args) {

        SpringApplication.run(CandidateOnboardingSystemApplication.class, args);
    }

}
