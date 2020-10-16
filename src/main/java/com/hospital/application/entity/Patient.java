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
public class Patient extends Person implements Serializable {

    /**
     * Fields describing the person number.
     */
    @NotNull
    @NotEmpty
    private String number;

    /**
     * Default constructor.
     */
    public Patient() {
    }

    /**
     * Function to get value of field {@link Patient#number}.
     *
     * @return returns person number.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Function to set value of field.
     *
     * @param number - mobil number.
     */
    public void setNumber(final String number) {
        this.number = number.trim();
    }

}
