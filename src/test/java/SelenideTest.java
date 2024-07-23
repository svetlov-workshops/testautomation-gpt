import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.Selenide.*;

public class SelenideTest {

    @BeforeClass
    public static void setupClass() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--no-sandbox");
        options.setCapability("marionette", true);
        options.setAcceptInsecureCerts(false);
        options.addArguments("--remote-allow-origins=*");

        Configuration.browserCapabilities = options;
    }

    @Test
    public void testTitle() {
        open("https://termine.bonn.de/m/auslaenderamt/extern/calendar/?uid=163e5a5b-3edb-4de1-97c7-b4922526085f");
        //check visible checked checkbox

        $("input[type='checkbox']").setSelected(true);
        // click on button with text "Weiter"
        $x("//button[contains(text(), 'Weiter')]").click();

        SelenideElement error = $x("//p[contains(text(), 'Keine freien Termine gefunden.')]");
        if (error.exists() && error.isDisplayed()) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }
    }

    @AfterClass
    public static void teardownClass() {
        WebDriverRunner.closeWebDriver();
    }
}