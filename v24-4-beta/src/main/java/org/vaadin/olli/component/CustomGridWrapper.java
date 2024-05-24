package org.vaadin.olli.component;

import com.vaadin.flow.component.html.Div;
import com.vaadin.mpr.LegacyWrapper;
import org.vaadin.olli.views.helloworld.CustomGrid;

public class CustomGridWrapper extends Div {
    public CustomGridWrapper() {
        LegacyWrapper lw = new LegacyWrapper(new CustomGrid());
        lw.setHeight("300px");
        lw.setWidth("300px");
        add(lw);
    }
}
