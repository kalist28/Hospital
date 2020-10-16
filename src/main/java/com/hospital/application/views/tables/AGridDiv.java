package com.hospital.application.views.tables;

import com.hospital.application.entity.Person;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.ValueProvider;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Hospital Application.
 * <p>
 * Preform for creating a page for viewing records of a specific entity.
 *
 * @param <C> entity.
 * @author Dmitriy Kalistratov <dmitry@kalistratov.ru>
 * @version 1.0
 */
public abstract class AGridDiv<C> extends VerticalLayout {

    /**
     * Status validation.
     */
    protected Div validStat;

    /**
     * Table of records entities.
     */
    protected Grid<C> grid;

    /**
     * Binder for adding new entries.
     */
    protected Binder<C> binder;

    /**
     * Editor for changing existing records.
     */
    protected Editor<C> editor;

    /**
     * Header for the data filling form.
     */
    protected HorizontalLayout header;

    /**
     * Master records repository.
     */
    protected JpaRepository<C, Long> repository;

    /**
     * Data provider for filtering.
     */
    protected ListDataProvider<C> dataProvider;

    /**
     * Constructor.
     * <p>
     * Initialization of the main parameters.
     *
     * @param repos    - records repository.
     * @param beanType - the record type.
     */
    protected AGridDiv(final JpaRepository<C, Long> repos,
                       final java.lang.Class<C> beanType) {
        this.grid = new Grid<>();
        this.binder = new Binder<>(beanType);
        this.editor = grid.getEditor();
        this.header = new HorizontalLayout();
        this.validStat = new Div();
        this.repository = repos;
        this.dataProvider = new ListDataProvider<C>(repository.findAll());
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        editor.setBinder(binder);
        editor.setBuffered(true);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        validStat.setId("validation");

        setHeightFull();

        grid.getElement().addEventListener("keyup", event -> editor.cancel())
                .setFilter("event.key === 'Escape' || event.key === 'Esc'");

        VerticalLayout layout = new VerticalLayout(validStat, grid);
        layout.setHeightFull();
        add(header, layout);
    }

    /**
     * Create a column and edit text field for edit mode.
     *
     * @param title         - title column.
     * @param bindName      - bind name.
     * @param valueProvider - data provider.
     * @param validator     - validation control
     */
    protected void createEditTextColumn(final String title,
                                        final String bindName,
                                        final ValueProvider<C, ?> valueProvider,
                                        final Validator validator) {
        Grid.Column<C> column = grid.addColumn(valueProvider).setHeader(title);
        TextField field = new TextField();
        binder.forField(field)
                .withValidator(validator)
                .withStatusLabel(validStat).bind(bindName);
        column.setEditorComponent(field);
    }

    /**
     * Create a column and edit date picker for edit mode.
     *
     * @param title         - title column.
     * @param bindName      - bind name.
     * @param valueProvider - data provider.
     */
    protected void createDatePickerColumn(final String title,
                                          final String bindName,
                                          final ValueProvider<C, ?> valueProvider) {
        Grid.Column<C> column = grid.addColumn(valueProvider).setHeader(title);
        DatePicker picker = new DatePicker();
        binder.forField(picker)
                .asRequired("Please choose a date")
                .withStatusLabel(validStat).bind(bindName);
        column.setEditorComponent(picker);

    }

    /**
     * Create a column and edit the drop-down list for edit mode.
     *
     * @param title         - title column.
     * @param bindName      - bind name.
     * @param valueProvider - data provider.
     * @param list          - options to choose from.
     */
    protected void createCheckBoxColumn(final String title,
                                        final String bindName,
                                        final ValueProvider<C, ?> valueProvider,
                                        final List list) {
        Grid.Column<C> column = grid.addColumn(valueProvider).setHeader(title);
        ComboBox<Person> box = new ComboBox<>();
        binder.forField(box)
                .withStatusLabel(validStat).bind(bindName);
        column.setEditorComponent(box);
        box.setItems(list);
        box.setPlaceholder(title);
        box.setRequired(true);
        box.setClearButtonVisible(true);
    }

    /**
     * Creating buttons for change mode.
     */
    protected void createEditBtms() {
        Collection<Button> editButtons
                = Collections.newSetFromMap(new WeakHashMap<>());

        Grid.Column<C> editorColumn = grid.addComponentColumn(person -> {
            Button edit = new Button("Edit");
            edit.addClassName("edit");
            edit.addClickListener(e -> editor.editItem(person));
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        });

        editor.addOpenListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!editor.isOpen())));
        editor.addCloseListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!editor.isOpen())));

        Button save = new Button("OK", new Icon(VaadinIcon.CHECK_CIRCLE), e -> {
            C c = editor.getItem();
            editor.save();
            repository.save(c);

            update();
        });
        save.addClassName("save");

        Button cancel
                = new Button("Отменить",
                new Icon(VaadinIcon.CHEVRON_CIRCLE_RIGHT),
                e -> editor.cancel()
        );
        cancel.addClassName("cancel");

        Icon delIco = new Icon(VaadinIcon.CLOSE_CIRCLE);
        delIco.setColor("red");
        Button delete = new Button("Удалить", delIco, e -> {
            try {
                repository.delete(editor.getItem());
                editor.cancel();
                update();
            } catch (DataIntegrityViolationException exception) {
                Dialog dialog = new Dialog();
                dialog.setCloseOnEsc(false);
                dialog.setCloseOnOutsideClick(false);
                dialog.setWidth("400px");
                dialog.setHeight("150px");
                VerticalLayout layout = new VerticalLayout(
                        new Text("Не удалось удалить запись," +
                                " так как она используется."),
                        new Button("Закрыть", event -> {
                            dialog.close();
                            editor.cancel();
                        })
                );
                layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
                dialog.add(layout);
                dialog.open();
            }
        });
        cancel.addClassName("delete");

        editorColumn.setEditorComponent(
                new VerticalLayout(
                        save,
                        cancel,
                        delete
                )
        );
    }

    /**
     * creating a unique header for each table.
     */
    protected abstract void createPanelAddEntry();

    /**
     * Update info about records.
     */
    protected void update() {
        grid.setItems(repository.findAll());
        this.dataProvider = new ListDataProvider<>(repository.findAll());
        grid.setDataProvider(dataProvider);
    }
}
