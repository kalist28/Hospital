package com.hospital.application.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Hospital Application.
 * <p>
 * An entity that describes a patient.
 *
 * @author Dmitriy Kalistratov <dmitry@kalistratov.ru>
 * @version 1.0
 */
@Entity
public class Doctor extends Person implements Serializable {

    /**
     * Fields doctor specialization.
     */
    @NotNull
    @NotEmpty
    private String specialization;

    /**
     * Fields number of prescriptions written.
     */
    @NotNull
    @NotEmpty
    private int countFormulas;

    /**
     * Default constructor.
     */
    public Doctor() {
    }

    /**
     * Function to set value of field.
     *
     * @param specialization - doctor specialization.
     */
    public void setSpecialization(final String specialization) {
        this.specialization = specialization.trim();
    }

    /**
     * Function to get value of field {@link Doctor#specialization}.
     *
     * @return returns doctor specialization.
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Function to set value of field.
     * increases the number of prescriptions written by 1.
     */
    public void addFormulas() {
        countFormulas++;
    }

    /**
     * Function to get value of field {@link Doctor#countFormulas}.
     *
     * @return returns count formulas.
     */
    public int getCountFormulas() {
        return countFormulas;
    }
}
