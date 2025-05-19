package com.example.candidateonboardingsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Data
@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public enum Role {
        ADMIN,
        HR,
        CANDIDATE
    }

}
