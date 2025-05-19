package com.example.candidateonboardingsystem.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/dashboard")
    public String adminDashboard() {
        logger.info("Admin dashboard accessed");
        return "Admin Access Granted";
    }
}
