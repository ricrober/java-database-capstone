package com.project.back_end.services;

import java.util.Date;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenService {

    // Added a logger for maintainability and clarity
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    @Value("${jwt.secret}")
    private String secret;

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public TokenService(AdminRepository adminRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .signWith(getSigningKey())
                .compact();
    } 

    public String extractEmail(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            logger.error("Token has expired: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.error("Invalid token signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Malformed JWT: {}", e.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("JWT parsing error: {}", e.getMessage());
        }
        return null;
    }

    public boolean validateToken(String token, String userRole) {
        String extractedEmail = extractEmail(token);
        
        if (extractedEmail == null) {
            logger.warn("Token validation failed: No email could be extracted.");
            return false;
        }

        boolean userExists = false;

        // Using a switch or clean if-else to check database presence
        switch (userRole.toLowerCase()) {
            case "admin":
                userExists = adminRepository.findByUsername(extractedEmail) != null;
                break;
            case "doctor":
                userExists = doctorRepository.findByEmail(extractedEmail) != null;
                break;
            case "patient":
                userExists = patientRepository.findByEmail(extractedEmail) != null;
                break;
            default:
                logger.warn("Unknown user role provided: {}", userRole);
                return false;
        }

        if (!userExists) {
            logger.warn("User with email {} and role {} not found in database.", extractedEmail, userRole);
        }

        return userExists;
    }
}
