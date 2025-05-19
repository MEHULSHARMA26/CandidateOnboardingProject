package com.example.candidateonboardingsystem.filter;

import com.example.candidateonboardingsystem.service.UserService;
import com.example.candidateonboardingsystem.utility.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        logger.info("Authorization header: {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            logger.info("JWT token extracted: {}", token);

            String username = null;
            try {
                username = jwtUtil.extractUsername(token);
                logger.info("Username extracted from token: {}", username);
            } catch (Exception e) {
                logger.error("Failed to extract username from token: {}", e.getMessage());
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    logger.info("User details loaded for username: {}", username);

                    if (jwtUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        logger.info("Authentication set in security context for user: {}", username);
                    } else {
                        logger.warn("JWT token validation failed for user: {}", username);
                    }
                } catch (Exception e) {
                    logger.error("Error during user authentication setup: {}", e.getMessage());
                }
            }
        } else {
            logger.warn("Authorization header missing or does not start with Bearer");
        }

        filterChain.doFilter(request, response);
    }
}
