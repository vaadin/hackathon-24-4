package org.vaadin.olli;

import com.vaadin.flow.component.WebComponentExporter;
import com.vaadin.flow.component.webcomponent.WebComponent;
import org.vaadin.olli.component.CustomGridWrapper;

public class LegacyCustomGridExporter extends WebComponentExporter<CustomGridWrapper> {

    public LegacyCustomGridExporter() {
        super("custom-grid");
    }

    @Override
    protected void configureInstance(WebComponent<CustomGridWrapper> webComponent,
                                     CustomGridWrapper customGridWrapper) {

    }
}
