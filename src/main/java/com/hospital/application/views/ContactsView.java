package com.hospital.application.views;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Hospital Application.
 * <p>
 * Contact information.
 *
 * @author Dmitriy Kalistratov <dmitry@kalistratov.ru>
 * @version 1.0
 */
@Route(value = "", layout = MainView.class)
public class ContactsView extends VerticalLayout {

    /**
     * Constructor.
     * <p>
     * Create developer info.
     */
    public ContactsView() {

        Label h1 = new Label("Developed by Dmitry Kalistratov");
        Label h2 = new Label("m.num +7 (904) 330-28-66");
        Label h3 = new Label("email dmitry@kalistratov.ru");
        VerticalLayout verticalLayout = new VerticalLayout(h1, h2, h3);
        verticalLayout.setSizeFull();
        verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(verticalLayout);
    }
}
