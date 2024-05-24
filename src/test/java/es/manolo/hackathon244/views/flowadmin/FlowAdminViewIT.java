
package es.manolo.hackathon244.views.flowadmin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vaadin.uitest.common.BasePlayWrightIT;

public class FlowAdminViewIT extends BasePlayWrightIT {

    @Override
    public String getUrl() {
        return "http://localhost:8080/data-grid";
    }

    @BeforeEach
    public void setupTest() throws Exception {
        super.setupTest();
        page.setDefaultTimeout(5000);
        fill(page.locator("vaadin-text-field"), "admin");
        fill(page.locator("vaadin-password-field"), "admin");
        click(page.locator("vaadin-button"));
        page.waitForURL(getUrl());
    }

    @Test
    public void userHasAccessToDataGrid() throws Exception {
        page.waitForSelector("vaadin-grid-pro");
        page.getByLabel("Select All").getByLabel("").check();
    }
}
