package com.example.candidateonboardingsystem.service;

import com.example.candidateonboardingsystem.model.CandidateModel;
import com.example.candidateonboardingsystem.model.JobOfferNotification;
import com.example.candidateonboardingsystem.repository.CandidateJobOfferNotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private static final int MAX_RETRIES = 3;

    private final CandidateJobOfferNotificationRepository jobOfferNotificationRepository;

    @Autowired
    public NotificationService(CandidateJobOfferNotificationRepository jobOfferNotificationRepository) {
        this.jobOfferNotificationRepository = jobOfferNotificationRepository;
    }

    public void handleTries(JobOfferNotification jobOfferNotification) {
        CandidateModel candidate = jobOfferNotification.getCandidate();

        if (candidate.getStatus() == CandidateModel.Status.APPLIED) {
            int currentRetries = jobOfferNotification.getRetry_count();

            if (currentRetries >= MAX_RETRIES) {
                jobOfferNotificationRepository.delete(jobOfferNotification);
                logger.info("Deleted JobOfferNotification for candidate {} after {} retries",
                        candidate.getId(), currentRetries);
            } else {
                jobOfferNotification.setRetry_count(currentRetries + 1);
                jobOfferNotificationRepository.save(jobOfferNotification);
                logger.info("Incremented retry count to {} for candidate {}",
                        currentRetries + 1, candidate.getId());
            }
        } else {
            logger.info("Candidate {} status is {}, no retry handling needed",
                    candidate.getId(), candidate.getStatus());
        }
    }
}
