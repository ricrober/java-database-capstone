package com.project.back_end.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.back_end.models.Patient;

/**
 * Repository interface for Patient entities.
 * Provides abstraction for storage, retrieval, and search operations 
 * on Patient data in the database.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Retrieves a patient record associated with a specific email address.
     * * @param email The unique email address of the patient.
     * @return The Patient object if found, otherwise null.
     */
    Patient findByEmail(String email);

    /**
     * Retrieves a patient record by searching for a match on either 
     * the email address or the phone number.
     * * @param email The email address to search for.
     * @param phone The phone number to search for.
     * @return The Patient object if a match is found for either field.
     */
    Patient findByEmailOrPhone(String email, String phone);
}
