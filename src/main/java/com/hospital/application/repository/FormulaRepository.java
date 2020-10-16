package com.hospital.application.repository;

import com.hospital.application.entity.Formula;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Hospital Application.
 * <p>
 * Bean repository for Formula.
 *
 * @author Dmitriy Kalistratov <dmitry@kalistratov.ru>
 * @version 1.0
 */
public interface FormulaRepository extends JpaRepository<Formula, Long> {
}
