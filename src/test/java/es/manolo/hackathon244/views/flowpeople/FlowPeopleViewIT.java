
package es.manolo.hackathon244.views.flowpeople;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.vaadin.uitest.common.BasePlayWrightIT;

public class FlowPeopleViewIT extends BasePlayWrightIT {

    private static final String URL = "http://localhost:8080/master-detail";

    @Override
    public String getUrl() {
        return URL;
    }

    @BeforeEach
    public void setupTest() throws Exception {
        super.setupTest();
        fill(page.locator("vaadin-text-field"), "admin");
        fill(page.locator("vaadin-password-field"), "admin");
        click(page.locator("vaadin-button"));
        page.waitForURL(getUrl());
    }

    @Test
    public void userCanModifyAnExistingUser() throws Exception {
        page.getByText("Eula", new Page.GetByTextOptions().setExact(true)).click();
        page.getByLabel("First Name", new Page.GetByLabelOptions().setExact(true)).click();
        page.getByLabel("First Name", new Page.GetByLabelOptions().setExact(true)).fill("MCM");
        page.getByLabel("First Name", new Page.GetByLabelOptions().setExact(true)).press("Tab");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        assertTrue(page.locator("vaadin-notification-card").textContent().contains("Data updated"));

        page.getByText("MCM", new Page.GetByTextOptions().setExact(true)).click();
        page.getByLabel("First Name", new Page.GetByLabelOptions().setExact(true)).click();
        page.getByLabel("First Name", new Page.GetByLabelOptions().setExact(true)).fill("MCM");
        page.getByLabel("First Name", new Page.GetByLabelOptions().setExact(true)).press("Tab");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        assertTrue(page.locator("vaadin-notification-card").textContent().contains("Data updated"));
    }

    @Test
    public void userCanCreateANewUser() throws Exception {

        page.getByLabel("First Name", new Page.GetByLabelOptions().setExact(true)).click();
        page.getByLabel("First Name", new Page.GetByLabelOptions().setExact(true)).fill("AAAAAAA");
        page.getByLabel("First Name", new Page.GetByLabelOptions().setExact(true)).press("Tab");
        page.getByLabel("Last Name", new Page.GetByLabelOptions().setExact(true)).fill("BB");
        page.getByLabel("Last Name", new Page.GetByLabelOptions().setExact(true)).press("Tab");
        page.getByLabel("Email", new Page.GetByLabelOptions().setExact(true)).fill("mcm@mcm.mcm");
        page.getByLabel("Email", new Page.GetByLabelOptions().setExact(true)).press("Tab");
        page.getByLabel("Phone", new Page.GetByLabelOptions().setExact(true)).fill("1231231");
        page.getByLabel("Phone", new Page.GetByLabelOptions().setExact(true)).press("Tab");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, EEEE", Locale.ENGLISH);
        String today = LocalDate.now().format(formatter);
        page.getByLabel("Date Of Birth", new Page.GetByLabelOptions().setExact(true)).fill(today);
        page.getByLabel(today).click();
        page.getByLabel("Date Of Birth", new Page.GetByLabelOptions().setExact(true)).press("Tab");

        page.getByLabel("Occupation", new Page.GetByLabelOptions().setExact(true)).click();
        page.getByLabel("Occupation", new Page.GetByLabelOptions().setExact(true)).fill("adffa");
        page.getByLabel("Occupation", new Page.GetByLabelOptions().setExact(true)).press("Tab");
        page.getByLabel("Role", new Page.GetByLabelOptions().setExact(true)).click();
        page.getByLabel("Role", new Page.GetByLabelOptions().setExact(true)).fill("admin");
        page.getByLabel("Role", new Page.GetByLabelOptions().setExact(true)).press("Tab");
        page.getByLabel("Important", new Page.GetByLabelOptions().setExact(true)).check();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();

        assertTrue(page.locator("vaadin-notification-card").textContent().contains("Data updated"));

        page.getByLabel("Sort by First Name").locator("div").nth(1).click();
        page.getByText("AAAAAAA").first().waitFor();
    }

}
