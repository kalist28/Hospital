package com.hospital.application.repository;

import com.hospital.application.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Hospital Application.
 * <p>
 * Bean repository for Patient.
 *
 * @author Dmitriy Kalistratov <dmitry@kalistratov.ru>
 * @version 1.0
 */
public interface PatientRepository extends JpaRepository<Patient, Long> {

}
