package com.hospital.application.views.tables;

import com.hospital.application.entity.Patient;
import com.hospital.application.repository.PatientRepository;
import com.hospital.application.views.MainView;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Hospital Application.
 * <p>
 * Table for entries of the essence of the Patient.
 *
 * @author Dmitriy Kalistratov <dmitry@kalistratov.ru>
 * @version 1.0
 */
@Route(value = "patients", layout = MainView.class)
@PageTitle("Пациенты")
@PreserveOnRefresh
public class PatientsView extends AGridDiv<Patient> {

    /**
     * Constructor.
     * <p>
     * Initializing table parameters.
     *
     * @param repository - records repository.
     */
    @Autowired
    public PatientsView(final PatientRepository repository) {
        super(repository, Patient.class);
        setId("patients-view");

        final int minLength = 3;
        final int maxLength = 20;

        createPanelAddEntry();

        grid.setItems(repository.findAll());
        grid.setHeight("70%");

        createEditTextColumn("Фамилия", "lastName",
                Patient::getLastName,
                new StringLengthValidator(
                        "Слишком длинная фамилия",
                        minLength,
                        maxLength
                )
        );
        createEditTextColumn("Имя", "name",
                Patient::getName,
                new StringLengthValidator(
                        "Слишком длинное имя",
                        minLength,
                        maxLength
                )
        );
        createEditTextColumn("Отчество", "patronymic",
                Patient::getPatronymic,
                new StringLengthValidator(
                        "Слишком длинное отчество",
                        minLength,
                        maxLength
                )
        );
        createEditTextColumn("Моб. номер", "number",
                Patient::getNumber,
                new RegexpValidator(
                        "Невалидный номер",
                        "(\\s*)?(\\+)?([- _():=+]?\\d[- _():=+]?){10,14}(\\s*)?"
                )
        );

        createEditBtms();
    }

    @Override
    protected void createPanelAddEntry() {
        final int minLength = 3;
        final int maxLength = 20;

        Binder<Patient> patientBinder = new Binder<>(Patient.class);

        TextField numberInput = new TextField("Номер телефона");
        numberInput.setClearButtonVisible(true);
        TextField nameInput = new TextField("Имя");
        nameInput.setMaxLength(maxLength);
        TextField lastNameInput = new TextField("Фамилия");
        lastNameInput.setMaxLength(maxLength);
        TextField patronymicInput = new TextField("Отчество");
        patronymicInput.setMaxLength(maxLength);

        patientBinder.forField(lastNameInput)
                .withValidator(
                        new StringLengthValidator(
                                "Фамилия не корректна",
                                minLength,
                                maxLength
                        )
                )
                .bind(Patient::getLastName, Patient::setLastName);
        patientBinder.forField(nameInput)
                .withValidator(
                        new StringLengthValidator(
                                "Имя не корректно",
                                minLength,
                                maxLength
                        )
                )
                .bind(Patient::getName, Patient::setName);
        patientBinder.forField(patronymicInput)
                .withValidator(
                        new StringLengthValidator(
                                "Отчество не корректно",
                                minLength,
                                maxLength
                        )
                )
                .bind(Patient::getPatronymic, Patient::setPatronymic);
        final String reg
                = "(\\s*)?(\\+)?([- _():=+]?\\d[- _():=+]?){10,14}(\\s*)?";
        patientBinder.forField(numberInput)
                .withValidator(
                        new RegexpValidator(
                                "Невалидный номер",
                                reg
                        )
                )
                .bind(Patient::getNumber, Patient::setNumber);

        Button btnAdd = new Button("Добавить");
        btnAdd.addClickListener(e -> {
            Patient p = new Patient();
            if (patientBinder.isValid()) {
                try {
                    patientBinder.writeBean(p);
                    patientBinder.getFields().forEach(HasValue::clear);
                    patientBinder.getFields().forEach(a -> {
                        TextField f = ((TextField) a);
                        f.setInvalid(false);
                    });
                    repository.save(p);
                    update();
                } catch (ValidationException ex) {
                    ex.printStackTrace();
                }
            }
        });

        header.add(
                nameInput,
                patronymicInput,
                lastNameInput,
                numberInput,
                btnAdd
        );
    }
}
