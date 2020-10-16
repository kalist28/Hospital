package com.hospital.application.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Hospital Application.
 * <p>
 * An entity that describes a formula.
 *
 * @author Dmitriy Kalistratov <dmitry@kalistratov.ru>
 * @version 1.0
 */
@Entity
public class Formula implements Serializable {

    public static final List<String> PRIORITIES = Arrays.asList("Немедленный", "Срочный", "Нормальный");

    /**
     * Unique ID field.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Formula description.
     */
    @NotNull
    @NotEmpty
    private String description;

    /**
     * Linking to doctor.
     */
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    /**
     * Linking to patient.
     */
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    /**
     * Status priority.
     */
    @NotNull
    private String priority;

    /**
     * Time when the recipe was created and valid.
     */
    @DateTimeFormat
    @NotNull
    private LocalDate creationDate, validity;

    /**
     * Default constructor.
     */
    public Formula() {
    }

    /**
     * Function to get value of field {@link Formula#id}.
     *
     * @return returns ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Function to set value of field.
     *
     * @param id - formula id.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Function to get value of field {@link Formula#description}.
     *
     * @return returns formula description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Function to set value of field.
     *
     * @param description - formula description.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Function to get value of field {@link Formula#doctor}.
     *
     * @return returns doctor.
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Function to get value of field .
     *
     * @return returns full doctor name.
     */
    public String getDoctorName() {
        return doctor.getLastName() + " " + doctor.getName();
    }

    /**
     * Function to set value of field.
     *
     * @param doctor - entity doctor.
     */
    public void setDoctor(final Doctor doctor) {
        this.doctor = doctor;
    }

    /**
     * Function to get value of field {@link Formula#patient}.
     *
     * @return returns patient.
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Function to get value of field.
     *
     * @return returns patient name.
     */
    public String getPatientName() {
        return patient.getLastName() + " " + patient.getName();
    }

    /**
     * Function to set value of field.
     *
     * @param patient - entity patient.
     */
    public void setPatient(final Patient patient) {
        this.patient = patient;
    }

    /**
     * Function to get value of field {@link Formula#priority}.
     *
     * @return returns status priority.
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Function to set value of field.
     *
     * @param priority - status priority.
     */
    public void setPriority(final String priority) {
        this.priority = priority.trim();
    }

    /**
     * Function to get value of field {@link Formula#creationDate}.
     *
     * @return returns creation date.
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }

    /**
     * Function to set value of field.
     *
     * @param creationDate - creation date.
     */
    public void setCreationDate(final LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Function to get value of field {@link Formula#validity}.
     *
     * @return returns validity date.
     */
    public LocalDate getValidity() {
        return validity;
    }

    /**
     * Function to set value of field.
     *
     * @param validity - validity date.
     */
    public void setValidity(final LocalDate validity) {
        this.validity = validity;
    }
}
