package org.vaadin.olli.views.helloworld;

import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.vaadin.olli.Person;

public class CustomGrid extends Grid<Person> {

    public CustomGrid() {
        setSizeFull();
        setItems(Person.getPersons());
        addColumn(Person::getFirstName).setCaption("First Name");
        addColumn(Person::getLastName).setCaption("Last Name");
        addColumn(p -> "<em>" + p.getEmail() + "</em>").setRenderer(new HtmlRenderer()).setCaption("Email");
        setItems(Person.getPersons());
        addSelectionListener(selectionEvent -> {
            Notification.show("Selected: " + selectionEvent.getFirstSelectedItem().get().getFirstName() + " " + selectionEvent.getFirstSelectedItem().get().getLastName() + " " + selectionEvent.getFirstSelectedItem().get().getEmail());
        });

    }
}
