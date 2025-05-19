package com.example.candidateonboardingsystem.config;

import com.example.candidateonboardingsystem.filter.JwtFilter;
import com.example.candidateonboardingsystem.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security filter chain...");

        http.csrf(AbstractHttpConfigurer::disable);
        logger.debug("CSRF protection disabled.");

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/hr/**").hasAuthority("HR")
                .anyRequest().authenticated()
        );
        logger.debug("Authorization rules configured.");

        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        logger.debug("Session management set to STATELESS.");

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        logger.info("JWT filter added before UsernamePasswordAuthenticationFilter.");

        logger.info("Security filter chain configuration completed.");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.warn("Using NoOpPasswordEncoder — not recommended for production.");
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        logger.info("Creating authentication manager...");
        return config.getAuthenticationManager();
    }

}
