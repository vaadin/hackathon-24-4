
package es.manolo.hackathon244.views.flowperson;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.vaadin.uitest.common.BasePlayWrightIT;

public class FlowPersonViewIT extends BasePlayWrightIT {

    @BeforeEach
    public void setupTest() throws Exception {
        super.setupTest();
        fill(page.locator("vaadin-text-field"), "user");
        fill(page.locator("vaadin-password-field"), "user");
        click(page.locator("vaadin-button"));
        page.waitForURL(getUrl());
    }

    @Override
    public String getUrl() {
        return "http://localhost:8080/person-form";
    }

    @Test
    public void userFillsOutTheContactForm() throws Exception {
        page.getByLabel("First Name").click();
        page.getByLabel("First Name").fill("AA");
        page.getByLabel("First Name").press("Tab");
        page.getByLabel("Last Name").fill("BB");
        page.getByLabel("Last Name").press("Tab");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, EEEE", Locale.ENGLISH);
        String today = LocalDate.now().format(formatter);
        page.getByLabel("Birthday").fill(today);
        page.getByLabel(today).click();

        page.getByLabel("Birthday").press("Tab");
        page.getByLabel("Phone Number").fill("1231231");
        page.getByLabel("Phone Number").press("Tab");
        page.getByLabel("Email").fill("mcm@mcm.mcm");
        page.getByLabel("Email").press("Tab");
        page.getByLabel("Occupation").fill("adffa");
        page.getByLabel("Occupation").press("Tab");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();

    }
}
