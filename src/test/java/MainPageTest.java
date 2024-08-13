import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Set;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainPageTest {

    @BeforeAll
    public static void setupClass() {
//        WebDriverManager.chromedriver().proxy("specialinternetaccess-lb.telekom.de:8080").setup();
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.setAcceptInsecureCerts(false);
        options.addArguments("--remote-allow-origins=*");

        Configuration.browserCapabilities = options;
    }

//    @Test
//    public void testTitle() {
//        open("https://termine.bonn.de/m/auslaenderamt/extern/calendar/?uid=163e5a5b-3edb-4de1-97c7-b4922526085f");
//        //check visible checked checkbox
//
//        $("input[type='checkbox']").setSelected(true);
//        // click on button with text "Weiter"
//        $x("//button[contains(text(), 'Weiter')]").click();
//
//        SelenideElement error = $x("//p[contains(text(), 'Keine freien Termine gefunden.')]");
//        if (error.exists() && error.isDisplayed()) {
//            System.out.println(true);
//        } else {
//            System.out.println(false);
//        }
//    }

    @Test
    public void verifyTopMenuItems() {
        open("http://testcon.lt");
        TopMenu.verifyMenuItemExists("REGISTER");
        TopMenu.verifyMenuItemExists("Home");
        TopMenu.verifyMenuItemExists("Schedule");
        TopMenu.verifyMenuItemExists("Sponsorship");
        TopMenu.verifyMenuItemExists("Contact Us");
        TopMenu.verifyMenuItemExists("Locations");
        TopMenu.verifyMenuItemExists("Previous Years");
    }

    @Test
    public void verifySubMenuItems() {
        open("http://testcon.lt");

        // Define top menu items and their corresponding sub-menu items
        String[][] subMenuItems = {
                {"Schedule", "Sessions"},
                {"Schedule", "Speakers"},
                {"Locations", "Venues"},
                {"Locations", "Hotels"},
                {"Previous Years", "Previous Editions"},
                {"Previous Years", "Records"},
                {"Previous Years", "Top Sessions"}
        };

        // Iterate through each top menu item and verify its sub-menu items
        for (String[] menuItem : subMenuItems) {
            TopMenu.clickMenuItem(menuItem[0]); // Click on the top menu item to make the sub-menu visible
            TopMenu.verifySubMenuItemExists(menuItem[0], menuItem[1]); // Verify the sub-menu item exists
        }
    }

    @ParameterizedTest
    @CsvSource({
            "Schedule,Sessions,/3253/#sessions",
            "Schedule,Speakers,/3253/#speakers",
            "Locations,Venues,testcon.lt/#",
            "Locations,Hotels,testcon.lt/hotels/",
    })
    public void testMenuNavigation(String menuItem, String subMenuItem, String expectedUrlPart) {
        open("http://testcon.lt");

        TopMenu.clickMenuItem(menuItem);
        TopMenu.clickSubMenuItem(menuItem, subMenuItem);

        // Check if a new window is opened
        String originalWindow = WebDriverRunner.getWebDriver().getWindowHandle();
        Set<String> windowHandles = WebDriverRunner.getWebDriver().getWindowHandles();

        if (windowHandles.size() > 1) {
            // Switch to the new window
            for (String windowHandle : windowHandles) {
                System.out.println(originalWindow);
                System.out.println(windowHandle);
                if (!windowHandle.equals(originalWindow)) {
                    WebDriverRunner.getWebDriver().switchTo().window(windowHandle);
                    break;
                }
            }
        }

        String currentUrl = WebDriverRunner.url();
        assertTrue(currentUrl.contains(expectedUrlPart),
                "Expected URL to contain: " + expectedUrlPart + " but was: " + currentUrl);

        // Switch back to the original window if a new window was opened
        if (windowHandles.size() > 1) {
            WebDriverRunner.getWebDriver().close();
            WebDriverRunner.getWebDriver().switchTo().window(originalWindow);
        }
    }


    @AfterAll
    public static void teardownClass() {
        WebDriverRunner.closeWebDriver();
    }
}