package com.example.candidateonboardingsystem.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hr")
public class HRController {

    private static final Logger logger = LoggerFactory.getLogger(HRController.class);

    @GetMapping("/dashboard")
    public String hrDashboard() {
        logger.info("HR dashboard accessed");
        return "HR Access Granted";
    }
}
