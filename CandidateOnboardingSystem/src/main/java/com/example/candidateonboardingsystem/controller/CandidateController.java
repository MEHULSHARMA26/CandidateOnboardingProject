package com.example.candidateonboardingsystem.controller;

import com.example.candidateonboardingsystem.dto.*;
import com.example.candidateonboardingsystem.model.CandidateDocument;
import com.example.candidateonboardingsystem.model.CandidateModel;
import com.example.candidateonboardingsystem.repository.CandidateDocumentRepository;
import com.example.candidateonboardingsystem.service.CandidateService;
import com.example.candidateonboardingsystem.service.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private static final Logger logger = LoggerFactory.getLogger(CandidateController.class);

    private final CandidateService candidateService;
    private final CandidateDocumentRepository candidateDocumentRepository;

    public CandidateController(CandidateService candidateService, CandidateDocumentRepository candidateDocumentRepository) {
        this.candidateService = candidateService;
        this.candidateDocumentRepository = candidateDocumentRepository;
    }

    @GetMapping("/all")
    public List<CandidateModel> getAllCandidates() {
        logger.info("Fetching all candidates...");
        return candidateService.getAllCandidates();
    }

    @GetMapping("/hired")
    public List<CandidateModel> getHiredCandidates() {
        logger.info("Fetching hired candidates...");
        return candidateService.getHiredCandidates();
    }

    @GetMapping("/onboarded")
    public List<CandidateModel> getAllOnBoardedCandidates() {
        logger.info("Fetching onboarded candidates...");
        return candidateService.getHiredCandidates();
    }

    @GetMapping("/count")
    public long getCandidateCount() {
        long count = candidateService.countCandidates();
        logger.info("Total candidate count: {}", count);
        return count;
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<String> updateCandidateStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        logger.info("Updating candidate status for ID: {}", id);
        try {
            CandidateModel.Status status = CandidateModel.Status.valueOf(request.getStatus().toUpperCase());
            boolean updated = candidateService.updateStatus(id, String.valueOf(status));
            if (updated) {
                logger.info("Candidate ID {} status updated to {}", id, status);
                return ResponseEntity.ok("Status updated");
            } else {
                logger.warn("Candidate ID {} not found for status update", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate not found");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid status value provided: {}", request.getStatus());
            return ResponseEntity.badRequest().body("Invalid status value");
        }
    }

    @PutMapping("/{id}/onboard-status")
    public ResponseEntity<String> updateOnboardingStatus(@PathVariable Long id, @RequestBody OnboardingStatusUpdateRequest request) {
        logger.info("Updating onboarding status for ID: {}", id);
        try {
            CandidateModel.OnboardingStatus onboardingStatus = CandidateModel.OnboardingStatus.valueOf(request.getOnboardingstatus().toUpperCase());
            boolean updated = candidateService.updateOnboardingStatus(id, String.valueOf(onboardingStatus));
            if (updated) {
                logger.info("Onboarding status updated for candidate ID {}: {}", id, onboardingStatus);
                return ResponseEntity.ok("Onboarding status updated");
            } else {
                logger.warn("Candidate ID {} not found for onboarding status update", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate not found");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid onboarding status value provided: {}", request.getOnboardingstatus());
            return ResponseEntity.badRequest().body("Invalid onboarding status value");
        }
    }

    @PutMapping("/{id}/personal-info")
    public ResponseEntity<String> updatePersonalInfo(@PathVariable Long id, @RequestBody PersonalInfoRequest request) {
        logger.info("Updating personal info for candidate ID {}", id);
        boolean updated = candidateService.updatePersonalInfo(id, request);
        if (updated) {
            logger.info("Personal info updated for candidate ID {}", id);
            return ResponseEntity.ok("Personal info updated successfully");
        } else {
            logger.warn("Candidate ID {} not found for personal info update", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate not found");
        }
    }

    @PutMapping("/{id}/bank-info")
    public ResponseEntity<String> updateBankInfo(@PathVariable Long id, @RequestBody BankInfoRequest request) {
        logger.info("Updating bank info for candidate ID {}", id);
        boolean updated = candidateService.updateBankInfo(id, request);
        if (updated) {
            logger.info("Bank info updated for candidate ID {}", id);
            return ResponseEntity.ok("Bank info updated successfully");
        } else {
            logger.warn("Candidate ID {} not found for bank info update", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate not found");
        }
    }

    @PutMapping("/{id}/educational-info")
    public ResponseEntity<String> updateEducationalInfo(@PathVariable Long id, @RequestBody EducationalInfoRequest request) {
        logger.info("Updating educational info for candidate ID {}", id);
        boolean updated = candidateService.updateEducationalInfo(id, request);
        if (updated) {
            logger.info("Educational info updated for candidate ID {}", id);
            return ResponseEntity.ok("Educational info updated successfully");
        } else {
            logger.warn("Candidate ID {} not found for educational info update", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate not found");
        }
    }

    @Autowired
    private CandidateService documentService;

    @PostMapping("/{id}/upload-document")
    public ResponseEntity<String> uploadDocument(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        logger.info("Uploading document for candidate ID {}", id);
        try {
            documentService.uploadDocument(id, file);
            logger.info("Document uploaded successfully for candidate ID {}", id);
            return ResponseEntity.ok("Document uploaded successfully");
        } catch (Exception e) {
            logger.error("Failed to upload document for candidate ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/verify-document")
    public ResponseEntity<String> verifyDocument(@PathVariable Long id) {
        logger.info("Verifying document with ID {}", id);
        Optional<CandidateDocument> optionalDoc = candidateDocumentRepository.findById(id);

        if (optionalDoc.isPresent()) {
            CandidateDocument document = optionalDoc.get();
            document.setFile_verified(true);
            candidateDocumentRepository.save(document);
            logger.info("Document verified successfully for document ID {}", id);
            return ResponseEntity.ok("Document verified successfully.");
        } else {
            logger.warn("Document not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Document not found with ID: " + id);
        }
    }

    @Autowired
    private EmailService emailService;

    @PostMapping("/send/{candidateId}")
    public ResponseEntity<String> sendMailToCandidate(@PathVariable Long candidateId) {
        logger.info("Sending email to candidate ID {}", candidateId);
        return emailService.sendMailToCandidate(candidateId);
    }
}
