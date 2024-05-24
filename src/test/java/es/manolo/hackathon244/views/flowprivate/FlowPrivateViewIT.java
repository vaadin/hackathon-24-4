
package es.manolo.hackathon244.views.flowprivate;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.vaadin.uitest.common.BasePlayWrightIT;

public class FlowPrivateViewIT extends BasePlayWrightIT {

    @BeforeEach
    public void setupTest() throws Exception {
        super.setupTest();
        // Login steps
        fill(page.locator("vaadin-text-field"), "user");
        fill(page.locator("vaadin-password-field"), "user");
        click(page.locator("vaadin-button"));
        page.waitForURL(getUrl());
    }

    @Override
    public String getUrl() {
        return "http://localhost:8080/hello-world3";
    }

    @Test
    public void userEntersNameAndClicksSayHelloButton() throws Exception {
        // Given the user is on the page HelloView
        page.waitForSelector("vaadin-text-field");

        // And the user has entered 'Jane Smith' in the text field with label 'Your name'
        fill(page.getByLabel("Your name"), "Jane Smith");
        // When the user clicks on the button with label 'Say hello'
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Say hello")).click();

        // Then a notification card with text 'Hello, Jane Smith' should appear
        page.waitForSelector("vaadin-notification-card");
        assertTrue(page.locator("vaadin-notification-card").textContent().contains("Jane Smith"));
    }
}
