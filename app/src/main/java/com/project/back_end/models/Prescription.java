package com.project.back_end.models;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Document(collection = "prescriptions")
public class Prescription {

	@Id
	private String id;

	@NotNull(message = "Patient name is required")
	@Size(min = 3, max = 100, message = "Patient name must be between 3 and 100 characters")
	private String patientName;

	@NotNull(message = "Appointment ID is required")
	private Long appointmentId;

	@NotNull(message = "Medication is required")
	@Size(min = 3, max = 100, message = "Medication must be between 3 and 100 characters")
	private String medication;

	@NotNull(message = "Dosage is required")
	private String dosage;

	@Size(max = 200, message = "Doctor notes cannot exceed 200 characters")
	private String doctorNotes;

	public Prescription() {}

	public Prescription(String id,
			@NotNull(message = "Patient name is required") @Size(min = 3, max = 100, message = "Patient name must be between 3 and 100 characters") String patientName,
			@NotNull(message = "Appointment ID is required") Long appointmentId,
			@NotNull(message = "Medication is required") @Size(min = 3, max = 100, message = "Medication must be between 3 and 100 characters") String medication,
			@NotNull(message = "Dosage is required") String dosage,
			@Size(max = 200, message = "Doctor notes cannot exceed 200 characters") String doctorNotes) {
		this.id = id;
		this.patientName = patientName;
		this.appointmentId = appointmentId;
		this.medication = medication;
		this.dosage = dosage;
		this.doctorNotes = doctorNotes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getMedication() {
		return medication;
	}

	public void setMedication(String medication) {
		this.medication = medication;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getDoctorNotes() {
		return doctorNotes;
	}

	public void setDoctorNotes(String doctorNotes) {
		this.doctorNotes = doctorNotes;
	}

}