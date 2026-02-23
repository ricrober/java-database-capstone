package com.project.back_end.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Entity class representing a Doctor in the system.
 * This class maps to the database and includes validation constraints 
 * for personal details and professional availability.
 */
@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Doctor's name cannot be null")
    @Size(min = 3, max = 100, message = "Doctor's name should be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Specialty cannot be null")
    @Size(min = 3, max = 50, message = "Specialty should be between 3 and 50 characters")
    private String specialty;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits long")
    private String phone;

    @ElementCollection
    private List<String> availableTimes;

    /**
     * Default constructor required by JPA.
     */
    public Doctor() {}

    /**
     * Overloaded constructor to initialize a Doctor with all fields.
     * * @param id Unique identifier
     * @param name Full name of the doctor
     * @param specialty Medical field of expertise
     * @param email Contact email address
     * @param password Account password (write-only)
     * @param phone 10-digit contact number
     * @param availableTimes List of scheduled time slots
     */
    public Doctor(Long id, String name, String specialty, String email, String password, String phone, List<String> availableTimes) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.availableTimes = availableTimes;
    }

    // --- Getters and Setters ---

    /** @return The unique ID of the doctor */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    /** @return The doctor's full name */
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    /** @return The medical specialty */
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    /** @return The doctor's email address */
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    /** @return The encoded password */
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    /** @return The 10-digit phone number */
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    /** @return List of available time slots for appointments */
    public List<String> getAvailableTimes() { return availableTimes; }
    public void setAvailableTimes(List<String> availableTimes) { this.availableTimes = availableTimes; }
}
