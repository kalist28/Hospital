package com.hospital.application.repository;

import com.hospital.application.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Hospital Application.
 * <p>
 * Bean repository for Doctor.
 *
 * @author Dmitriy Kalistratov <dmitry@kalistratov.ru>
 * @version 1.0
 */
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
