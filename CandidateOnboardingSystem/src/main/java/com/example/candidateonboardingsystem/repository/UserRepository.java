package com.example.candidateonboardingsystem.repository;

import com.example.candidateonboardingsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // method to check if username exists
    boolean existsByUsername(String username);

    //method to find user by username for login
    Optional<User> findByUsername(String username);

}
