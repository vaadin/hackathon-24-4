
package es.manolo.hackathon244.views.flowpublic;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vaadin.uitest.common.BasePlayWrightIT;

public class FlowPublicViewIT extends BasePlayWrightIT {

    @Override
    public String getUrl() {
        return "http://localhost:8080/";
    }
    
//    @BeforeEach
//    public void setupTest() throws Exception {
//        super.setupTest();
//        // Login steps
//        fill(page.locator("vaadin-text-field"), "admin");
//        fill(page.locator("vaadin-password-field"), "admin");
//        click(page.locator("vaadin-button"));
//        page.waitForTimeout(50);
//    }


    @Test
    public void initialHelloView() throws Exception {
        // Given the user is on the page HelloView
        page.waitForSelector("vaadin-text-field");
        page.waitForSelector("vaadin-button");

        // Then the user should see a text field with label 'Your name'
        assertTrue(page.locator("vaadin-text-field").count() > 0);

        // And the user should see a button with label 'Say hello'
        assertTrue(page.locator("vaadin-button").count() > 0);
    }

    @Test
    public void userEntersNameAndClicksButton() throws Exception {
        // Given the user is on the page HelloView
        page.waitForSelector("vaadin-text-field");
        page.waitForSelector("vaadin-button");

        // And the user has entered 'Jane Smith' in the text field with label 'Your name'
        fill(page.locator("vaadin-text-field").first(), "Jane Smith");

        // When the user clicks on the button with label 'Say hello'
        click(page.locator("vaadin-button").first());

        // Then a paragraph with text 'Hello, Jane Smith' should appear
        page.waitForSelector("vaadin-notification-card");
        assertTrue(page.locator("vaadin-notification-card").count() > 0);
    }
}
