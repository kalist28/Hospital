package com.hospital.application.views;

import com.hospital.application.views.tables.DoctorsView;
import com.hospital.application.views.tables.FormulasView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.hospital.application.views.tables.PatientsView;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * Hospital Application.
 * <p>
 * Main container for layouts.
 *
 * @author Dmitriy Kalistratov <dmitry@kalistratov.ru>
 * @version 1.0
 */
@JsModule("./styles/shared-styles.js")
@CssImport("./styles/views/main/main-view.css")
@PWA(name = "Hospital", shortName = "Hospital", enableInstallPrompt = false)
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class MainView extends AppLayout {

    /**
     * Constructor.
     * <p>
     * Initialize a title and tabs.
     */
    public MainView() {
        setPrimarySection(Section.DRAWER);
        Tabs menu = createMenu();
        addToNavbar(true, createHeaderContent(menu));
        menu.setSelectedIndex(0);
    }

    /**
     * Create a header context, set a theme, and add tabs.
     *
     * @param menu - page tabs.
     * @return layout of the header.
     */
    private Component createHeaderContent(final Tabs menu) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(menu);
        return layout;
    }

    /**
     * Creating a tab menu.
     *
     * @return page tabs.
     */
    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    /**
     * Creating an array of tabs.
     *
     * @return array tabs.
     */
    private Component[] createMenuItems() {
        return new Tab[]{
                createTab("Пациенты", PatientsView.class),
                createTab("Доктора", DoctorsView.class),
                createTab("Рецепты", FormulasView.class),
                createTab("Контакты", ContactsView.class)
        };
    }

    /**
     * Creating a tab.
     *
     * @param text      - tab title.
     * @param component - class of the tab.
     * @return tab.
     */
    private static Tab createTab(final String text,
                                 final Class<? extends Component> component) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, component));
        ComponentUtil.setData(tab, Class.class, component);
        return tab;
    }

}
