package com.hospital.application.views.tables;

import com.hospital.application.entity.Doctor;
import com.hospital.application.repository.DoctorRepository;
import com.hospital.application.views.MainView;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Hospital Application.
 * <p>
 * Table for entries of the essence of the Doctors.
 *
 * @author Dmitriy Kalistratov <dmitry@kalistratov.ru>
 * @version 1.0
 */
@Route(value = "doctors", layout = MainView.class)
@PageTitle("Доктора")
@PreserveOnRefresh
public class DoctorsView extends AGridDiv<Doctor> {

    /**
     * Constructor.
     * <p>
     * Initialization of the main parameters.
     *
     * @param repository - main records repository.
     */
    @Autowired
    protected DoctorsView(final DoctorRepository repository) {
        super(repository, Doctor.class);
        final int minLength = 3;
        final int maxLength = 20;
        setId("doctors-view");

        createPanelAddEntry();

        grid.setItems(repository.findAll());
        grid.setHeight("70%");
        grid.setItemDetailsRenderer(TemplateRenderer.<Doctor>of(
                "<div class='custom-details' style='border: 1px solid gray; padding: 10px; width: 100%; box-sizing: border-box;'>"
                        + "<div>[[item.name]] [[item.lastName]] выписал <b>[[item.count]]</b> рецептов.</div>"
                        + "</div>")
                .withProperty("count", Doctor::getCountFormulas)
                .withProperty("name", Doctor::getName)
                .withProperty("lastName", Doctor::getLastName)
                .withEventHandler("handleClick", person -> {
                    grid.getDataProvider().refreshItem(person);
                }));

        createEditTextColumn(
                "Фамилия",
                "lastName",
                Doctor::getLastName,
                new StringLengthValidator(
                        "Слишком длинная фамилия",
                        minLength,
                        maxLength
                )
        );
        createEditTextColumn(
                "Имя",
                "name",
                Doctor::getName,
                new StringLengthValidator(
                        "Слишком длинное имя",
                        minLength,
                        maxLength
                )
        );
        createEditTextColumn(
                "Отчество",
                "patronymic",
                Doctor::getPatronymic,
                new StringLengthValidator(
                        "Слишком длинное отчество",
                        minLength,
                        maxLength
                )
        );
        createEditTextColumn(
                "Специализация",
                "specialization",
                Doctor::getSpecialization,
                new StringLengthValidator(
                        "Слишком длинное отчество",
                        minLength,
                        maxLength
                )
        );

        createEditBtms();
    }

    @Override
    protected void createPanelAddEntry() {
        final int minLength = 3;
        final int maxLength = 20;

        Binder<Doctor> patientBinder = new Binder<>(Doctor.class);

        TextField numberInput = new TextField("Специализация");
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
                ).bind(Doctor::getLastName, Doctor::setLastName);
        patientBinder.forField(nameInput)
                .withValidator(
                        new StringLengthValidator(
                                "Имя не корректно",
                                minLength,
                                maxLength
                        )
                ).bind(Doctor::getName, Doctor::setName);
        patientBinder.forField(patronymicInput)
                .withValidator(
                        new StringLengthValidator(
                                "Отчество не корректно",
                                minLength,
                                maxLength
                        )
                ).bind(Doctor::getPatronymic, Doctor::setPatronymic);
        patientBinder.forField(numberInput)
                .withValidator(
                        new StringLengthValidator(
                                "Специализация не корректна",
                                minLength,
                                maxLength
                        )
                ).bind(Doctor::getSpecialization, Doctor::setSpecialization);

        Button btnAdd = new Button("Добавить");
        btnAdd.addClickListener(e -> {
            Doctor p = new Doctor();
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
