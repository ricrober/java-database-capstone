package com.project.back_end.services;

import java.util.Date;
import javax.crypto.SecretKey;

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
import io.jsonwebtoken.security.Keys;

/**
 * Service responsible for JWT (JSON Web Token) operations including 
 * generation, extraction, and validation for different user roles.
 */
@Component
public class TokenService {

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

    /**
     * Converts the configured secret string into a cryptographic SecretKey.
     * @return A HMAC-SHA Key used for signing and verifying tokens.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generates a new JWT for a specific user email.
     * The token is valid for 7 days from the issued date.
     * * @param email The subject (identifier) to be encoded in the token.
     * @return A compact URL-safe JWT string.
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .signWith(getSigningKey())
                .compact();
    }    

    /**
     * Extracts the email (subject) from a provided JWT.
     * Handles potential parsing exceptions gracefully to ensure system stability.
     * * @param token The JWT string to parse.
     * @return The extracted email if valid, or null if the token is invalid or expired.
     */
    public String extractEmail(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            // Log this error in a real production environment
            return null;
        }
    }

    /**
     * Validates a token by checking its integrity and confirming the user 
     * exists in the database based on their role.
     * * @param token The JWT string to validate.
     * @param user The role type ("admin", "doctor", or "patient").
     * @return true if the token is valid and the user exists; false otherwise.
     */
    public boolean validateToken(String token, String user) {
        try {
            String extracted = extractEmail(token);
            if (extracted == null) return false;

            if (user.equals("admin")) {
                Admin admin = adminRepository.findByUsername(extracted);
                return admin != null;
            } 
            else if (user.equals("doctor")) {
                Doctor doctor = doctorRepository.findByEmail(extracted);
                return doctor != null;
            } 
            else if (user.equals("patient")) {
                Patient patient = patientRepository.findByEmail(extracted);
                return patient != null;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
