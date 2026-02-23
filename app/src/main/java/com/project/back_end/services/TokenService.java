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

    /**
     * The raw secret string used for signing and verifying JWTs.
     * This value is injected from the application properties file 
     * (e.g., application.properties or application.yml) using the key 'jwt.secret'.
     */
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
     * Converts the raw 'secret' string into a HMAC-SHA SecretKey.
     * This method utilizes the 'secret' field to ensure all tokens are 
     * cryptographically signed and verified using a consistent key.
     * * @return A SecretKey derived from the 'secret' field.
     */
    private SecretKey getSigningKey() {
        // Encodes the secret string into bytes and generates the HMAC key
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generates a new JWT for a specific user email.
     * The token is signed using the key derived from the 'secret' field.
     * * @param email The subject (identifier) to be encoded in the token.
     * @return A compact URL-safe JWT string.
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .signWith(getSigningKey()) // Uses the secret-based key here
                .compact();
    }    

    /**
     * Extracts the email (subject) from a provided JWT.
     * Verifies the token's signature using the secret-based key before extraction.
     * * @param token The JWT string to parse.
     * @return The extracted email if valid, or null if invalid/expired.
     */
    public String extractEmail(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey()) // Verifies against the configured secret
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Validates a token by checking its integrity and confirming the user 
     * exists in the database based on their role.
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
