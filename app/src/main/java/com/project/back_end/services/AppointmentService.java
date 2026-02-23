package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import jakarta.transaction.Transactional;

/**
 * Service class for managing appointment bookings, updates, and cancellations.
 * Includes logging for better observability and detailed response handling.
 */
@Service
public class AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    private final AppointmentRepository appointmentRepository;
    private final com.project.back_end.services.Service service;
    private final TokenService tokenService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
            com.project.back_end.services.Service service, TokenService tokenService,
            PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.service = service;
        this.tokenService = tokenService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    /**
     * Persists a new appointment in the database.
     * @param appointment The appointment entity to save.
     * @return 1 if successful, 0 otherwise.
     */
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            logger.info("Successfully booked appointment for Patient ID: {}", appointment.getPatient().getId());
            return 1;
        } catch (Exception e) {
            logger.error("Failed to book appointment: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Updates an existing appointment after validating business rules.
     */
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> existingOpt = appointmentRepository.findById(appointment.getId());
        if (existingOpt.isEmpty()) {
            logger.warn("Update failed: No appointment found with ID {}", appointment.getId());
            response.put("message", "Update failed: No appointment exists with the provided ID.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Validate that the appointment belongs to the correct patient
        if (existingOpt.get().getPatient().getId() != appointment.getPatient().getId()) {
            logger.error("Update failed: Patient ID mismatch for Appointment ID {}", appointment.getId());
            response.put("message", "Security Alert: You cannot update an appointment belonging to another patient.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        int validationResult = service.validateAppointment(appointment);
        if (validationResult == 1) {
            try {
                appointmentRepository.save(appointment);
                logger.info("Appointment ID {} updated successfully", appointment.getId());
                response.put("message", "Appointment details have been updated successfully.");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                logger.error("Database error during update for Appointment {}: {}", appointment.getId(), e.getMessage());
                response.put("message", "A technical error occurred while saving the update.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else if (validationResult == -1) {
            response.put("message", "Validation failed: The specified doctor does not exist.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.put("message", "Slot unavailable: The doctor is already booked at this time or is not available.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Cancels an appointment if the token matches the patient identity.
     */
    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
        Map<String, String> response = new HashMap<>();
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);

        if (appointmentOpt.isEmpty()) {
            response.put("message", "Cancellation failed: No appointment found with ID " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        String email = tokenService.extractEmail(token);
        Patient patient = patientRepository.findByEmail(email);

        if (patient == null || !patient.getId().equals(appointmentOpt.get().getPatient().getId())) {
            logger.warn("Unauthorized cancellation attempt for Appointment ID {} by User {}", id, email);
            response.put("message", "Unauthorized: You do not have permission to cancel this appointment.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            appointmentRepository.delete(appointmentOpt.get());
            logger.info("Appointment ID {} cancelled by Patient {}", id, email);
            response.put("message", "Appointment has been successfully cancelled.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error deleting appointment {}: {}", id, e.getMessage());
            response.put("message", "An error occurred while processing the cancellation.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Transactional
    public Map<String, Object> getAppointment(String pname, LocalDate date, String token) {
        Map<String, Object> map = new HashMap<>();
        String extractedEmail = tokenService.extractEmail(token);
        
        var doctor = doctorRepository.findByEmail(extractedEmail);
        if (doctor == null) {
            logger.warn("Fetch failed: No doctor associated with email {}", extractedEmail);
            map.put("appointments", List.of());
            return map;
        }

        Long doctorId = doctor.getId();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Appointment> appointments;
        if (pname == null || pname.equalsIgnoreCase("null") || pname.isEmpty()) {
            appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, startOfDay, endOfDay);
        } else {
            appointments = appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                    doctorId, pname, startOfDay, endOfDay);
        }

        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(app -> new AppointmentDTO(
                        app.getId(),
                        app.getDoctor().getId(),
                        app.getDoctor().getName(),
                        app.getPatient().getId(),
                        app.getPatient().getName(),
                        app.getPatient().getEmail(),
                        app.getPatient().getPhone(),
                        app.getPatient().getAddress(),
                        app.getAppointmentTime(),
                        app.getStatus()))
                .collect(Collectors.toList());

        map.put("appointments", appointmentDTOs);
        return map;
    }

    @Transactional
    public void changeStatus(long appointmentId) {
        logger.info("Updating status for Appointment ID: {}", appointmentId);
        appointmentRepository.updateStatus(1, appointmentId);
    }
}
