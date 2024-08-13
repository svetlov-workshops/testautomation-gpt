import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$;

public class TopMenu {
    public static void verifyMenuItemExists(String menuItemName) {
        ElementsCollection menuItems = $$("#top-menu li a");
        SelenideElement item = menuItems.find(Condition.text(menuItemName));
        item.shouldBe(Condition.visible);
    }

    public static void verifySubMenuItemExists(String menuItemName, String subMenuItemName) {
        $$("#top-menu li a").find(Condition.text(menuItemName)).parent().$$(".sub-menu li a").find(Condition.text(subMenuItemName)).should(Condition.exist);
    }

    //method to click on menu or sub menu if provided:
    public static void clickMenuItem(String menuItemName) {
        $$("#top-menu li a").find(Condition.text(menuItemName)).click();
    }

    public static void clickSubMenuItem(String menuItemName, String subMenuItemName) {
        $$("#top-menu li a").find(Condition.text(menuItemName)).parent().$$(".sub-menu li a").find(Condition.text(subMenuItemName)).click();
    }
}
