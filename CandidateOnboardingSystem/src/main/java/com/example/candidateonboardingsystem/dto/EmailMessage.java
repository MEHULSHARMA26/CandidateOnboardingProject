package com.example.candidateonboardingsystem.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailMessage {
    private String to;
    private String subject;
    private String body;

}
