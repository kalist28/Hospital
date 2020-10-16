package com.hospital.application.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Hospital Application.
 * <p>
 * An entity that describes a person.
 *
 * @author Dmitriy Kalistratov <dmitry@kalistratov.ru>
 * @version 1.0
 */
@MappedSuperclass
public abstract class Person implements Serializable {

    /**
     * Unique ID field.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Fields describing the person's full name.
     */
    @NotNull
    @NotEmpty
    private String name, lastName, patronymic;

    /**
     * Default constructor.
     */
    public Person() {
    }

    /**
     * Function to get value of field {@link Person#id}.
     *
     * @return returns ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Function to set value of field.
     *
     * @param id - id word in journal.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Function to set value of field.
     *
     * @param name - person name.
     */
    public void setName(final String name) {
        this.name = name.trim();
    }

    /**
     * Function to set value of field.
     *
     * @param lastName - person last name.
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName.trim();
    }

    /**
     * Function to set value of field.
     *
     * @param patronymic - person patronymic.
     */
    public void setPatronymic(final String patronymic) {
        this.patronymic = patronymic.trim();
    }

    /**
     * Function to get value of field {@link Person#name}.
     *
     * @return returns person name.
     */
    public String getName() {
        return name;
    }

    /**
     * Function to get value of field {@link Person#lastName}.
     *
     * @return returns person last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Function to get value of field {@link Person#patronymic}.
     *
     * @return returns person patronymic.
     */
    public String getPatronymic() {
        return patronymic;
    }

    @Override
    public String toString() {
        return getLastName() + " " + getName();
    }
}
