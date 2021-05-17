package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.io.File;

public class HelperBase {
    protected WebDriver driver;

    public HelperBase(WebDriver driver) {
        this.driver = driver;
    }

    protected void click(By locator) {
        driver.findElement(locator).click();
    }

    public boolean isClickable(By locator) {
        WebElement element = driver.findElement(locator);

        return element.isEnabled() && element.isDisplayed();
    }

    protected void type(By locator, String text) {
        click(locator);
        if (text != null) {
            String existingText = locator.findElement(driver).getAttribute("value");
            if (! text.equals(existingText)) {
                driver.findElement(locator).clear();
                driver.findElement(locator).sendKeys(text);
            }
        }
    }

    protected void select(By locator, String text) {
        click(locator);

        if (text != null) {
            Select select = new Select(locator.findElement(driver));
            select.selectByValue(text);
        }
    }

    protected void attach(By locator, File file) {
        if (file != null) {
            driver.findElement(locator).sendKeys(file.getAbsolutePath());
        }
    }

    public boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }
}
