package com.hospital.application.views.tables;

import com.hospital.application.entity.Doctor;
import com.hospital.application.entity.Formula;
import com.hospital.application.entity.Patient;
import com.hospital.application.repository.DoctorRepository;
import com.hospital.application.repository.FormulaRepository;
import com.hospital.application.repository.PatientRepository;
import com.hospital.application.views.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * Hospital Application.
 * <p>
 * Table for entries of the essence of the Formulas.
 *
 * @author Dmitriy Kalistratov <dmitry@kalistratov.ru>
 * @version 1.0
 */
@PageTitle("Рецепты")
@Route(value = "formulas", layout = MainView.class)
@PreserveOnRefresh
public class FormulasView extends AGridDiv<Formula> {

    /**
     * Doctor records repository.
     */
    private final DoctorRepository doctorRepository;

    /**
     * Patient records repository.
     */
    private final PatientRepository patientRepository;

    /**
     * Constructor.
     * <p>
     * Initialization of the main parameters.
     *
     * @param pRepository - doctor records repository.
     * @param dRepository - patient records repository.
     * @param repository  - main records repository.
     */
    @Autowired
    protected FormulasView(final PatientRepository pRepository,
                           final DoctorRepository dRepository,
                           final FormulaRepository repository) {
        super(repository, Formula.class);
        setId("formulas-view");

        patientRepository = pRepository;
        doctorRepository = dRepository;

        createPanelAddEntry();

        grid.setItems(repository.findAll());
        grid.setHeight("70%");

        createEditTextColumn(
                "Описание",
                "description",
                Formula::getDescription,
                new StringLengthValidator(
                        "Слишком длинное описание",
                        3,
                        200
                )
        );
        createCheckBoxColumn(
                "Доктор",
                "doctor",
                Formula::getDoctorName,
                doctorRepository.findAll()
        );
        createCheckBoxColumn(
                "Пациент",
                "patient",
                Formula::getPatientName,
                patientRepository.findAll()
        );
        createCheckBoxColumn(
                "Приоритет",
                "priority",
                Formula::getPriority,
                Formula.PRIORITIES
        );
        createDatePickerColumn(
                "Создание",
                "creationDate",
                Formula::getCreationDate
        );
        createDatePickerColumn(
                "Срок годности",
                "validity", Formula::getValidity
        );

        addFilterFields();
        createEditBtms();
    }

    /**
     * Setting fields for filtering in the table.
     */
    private void addFilterFields() {
        HeaderRow filterRow = grid.appendHeaderRow();

        grid.setDataProvider(dataProvider);

        TextField descriptionField = new TextField();
        descriptionField.setPlaceholder("Filter");
        descriptionField.addValueChangeListener(event -> dataProvider.addFilter(
                formula -> StringUtils.containsIgnoreCase(
                        formula.getDescription(),
                        descriptionField.getValue()
                )
        ));
        descriptionField.setClearButtonVisible(true);
        descriptionField.setValueChangeMode(ValueChangeMode.EAGER);
        descriptionField.setSizeFull();

        ComboBox<Patient> patientFilterComboBox = new ComboBox<>();
        setComboXox(
                patientFilterComboBox,
                "Filter",
                patientRepository.findAll()
        );
        patientFilterComboBox.addValueChangeListener(
                event -> dataProvider.addFilter(
                        formula -> {
                            String filter;
                            try {
                                filter = patientFilterComboBox.getValue().toString();
                            } catch (Exception e) {
                                filter = "";
                            }
                            return StringUtils.containsIgnoreCase(
                                    formula.getPatient().toString(),
                                    filter
                            );
                        }));
        patientFilterComboBox.setClearButtonVisible(true);

        ComboBox<String> priorityFilterComboBox = new ComboBox<>();
        setComboXox(priorityFilterComboBox, "Filter", Formula.PRIORITIES);
        priorityFilterComboBox.addValueChangeListener(
                event -> dataProvider.addFilter(
                        formula -> {
                            String filter;
                            try {
                                filter = priorityFilterComboBox.getValue().trim();
                            } catch (Exception e) {
                                filter = "";
                            }
                            return StringUtils.containsIgnoreCase(
                                    formula.getPriority(),
                                    filter
                            );
                        }
                )
        );
        priorityFilterComboBox.setClearButtonVisible(true);
        filterRow.getCell(
                grid.getColumns().get(0)).setComponent(descriptionField);
        filterRow.getCell(
                grid.getColumns().get(2)).setComponent(patientFilterComboBox);
        filterRow.getCell(
                grid.getColumns().get(3)).setComponent(priorityFilterComboBox);
    }

    @Override
    protected void createPanelAddEntry() {
        Binder<Formula> binder = new Binder<>();
        FormLayout columnLayout = new FormLayout();

        ComboBox<Doctor> doctorComboBox = new ComboBox<>();
        ComboBox<Patient> patientComboBox = new ComboBox<>();
        ComboBox<String> priorityComboBox = new ComboBox<>();


        setComboXox(priorityComboBox, "Приоритет рецепта", Formula.PRIORITIES);
        setComboXox(doctorComboBox, "Доктора", doctorRepository.findAll());
        setComboXox(patientComboBox, "Пациенты", patientRepository.findAll());

        TextArea textArea = new TextArea("Description");
        textArea.setPlaceholder("Write here ...");

        DatePicker datePicker1 = new DatePicker();
        datePicker1.setLabel("Дата создания");

        DatePicker datePicker2 = new DatePicker();
        datePicker2.setLabel("Срок годности");

        binder.forField(datePicker1).asRequired("Please choose a date")
                .bind(Formula::getCreationDate, Formula::setCreationDate);
        binder.forField(datePicker2).asRequired("Please choose a date")
                .bind(Formula::getValidity, Formula::setValidity);
        binder.forField(doctorComboBox)
                .bind(Formula::getDoctor, Formula::setDoctor);
        binder.forField(patientComboBox)
                .bind(Formula::getPatient, Formula::setPatient);
        binder.forField(priorityComboBox)
                .bind(Formula::getPriority, Formula::setPriority);
        binder.forField(textArea)
                .bind(Formula::getDescription, Formula::setDescription);

        Dialog dialog = new Dialog(columnLayout);

        Button button = new Button("Сохранить", event -> {
            if (binder.isValid()) {

                try {
                    Formula formula = new Formula();
                    binder.writeBean(formula);
                    Doctor doctor = formula.getDoctor();
                    doctor.addFormulas();
                    doctorRepository.save(doctor);
                    repository.save(formula);
                    update();
                    dialog.close();
                } catch (ValidationException e) {
                    e.printStackTrace();
                }

            }
        }
        );

        columnLayout.add(
                patientComboBox,
                doctorComboBox,
                datePicker1,
                datePicker2,
                textArea,
                priorityComboBox,
                button
        );
        columnLayout.setColspan(textArea, 2);

        header.add(new Button("Создать новый рецепт", e -> dialog.open()));
        setDefaultHorizontalComponentAlignment(Alignment.END);
    }

    /**
     * Setting common values for the Combobox component.
     *
     * @param box  - component.
     * @param name - text in placeholder.
     * @param list - options in the drop-down list.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void setComboXox(final ComboBox box,
                             final String name,
                             final Collection list) {
        box.setItems(list);
        box.setPlaceholder(name);
        box.setRequired(true);
        box.setClearButtonVisible(true);
    }
}
