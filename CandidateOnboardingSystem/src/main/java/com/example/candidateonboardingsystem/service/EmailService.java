package com.example.candidateonboardingsystem.service;

import com.example.candidateonboardingsystem.model.CandidateModel;
import com.example.candidateonboardingsystem.repository.CandidateModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private CandidateModelRepository candidateRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        logger.info("Email sent to: {}", to);
    }

    public ResponseEntity<String> sendMailToCandidate(Long candidateId) {
        Optional<CandidateModel> optionalCandidate = candidateRepository.findById(candidateId);

        if (optionalCandidate.isPresent()) {
            CandidateModel candidate = optionalCandidate.get();
            logger.info("Candidate found: {}", candidate);
            String email = candidate.getEmail();
            String subject = "Job Notification";
            String body = "Dear " + candidate.getFirst_name() + " " + candidate.getLast_name() + ", you have a job offer!";
            sendEmail(email, subject, body);

            return ResponseEntity.ok("Email sending process triggered.");
        } else {
            logger.warn("Candidate NOT found with id: {}", candidateId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate NOT found with id: " + candidateId);
        }
    }

}
