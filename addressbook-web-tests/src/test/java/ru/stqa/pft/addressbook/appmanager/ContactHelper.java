package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactHelper extends HelperBase {

    public ContactHelper(WebDriver driver) {
        super(driver);
    }

    public void submitContactCreation() {
      click(By.cssSelector("input:nth-child(87)"));
    }

    public void fillContactForm(ContactData contactData, boolean creation) {
        type(By.name("firstname"), contactData.getFirstname());
        type(By.name("lastname"), contactData.getLastname());
        type(By.name("home"), contactData.getNumber_home());
        type(By.name("email"), contactData.getEmail());

        if (creation) {
            new Select(driver.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroup());
        } else {
            Assert.assertFalse(isElementPresent(By.name("new_group")));
        }
    }


    public void initContactCreation() {
      click(By.linkText("add new"));
    }

    public void editContactForm(int index) {
        driver.findElements(By.xpath("(//img[@alt='Edit'])")).get(index).click();
    }

    public void selectContact(int index) {
        driver.findElements(By.name("selected[]")).get(index).click();
    }

    private void selectContactById(int id) {
        driver.findElement(By.cssSelector("input[value='" + id + "']")).click();
    }

    public void updateContactModification() {
        click(By.xpath("(//input[@name='update'])[2]"));
    }

    public void deleteContact() {
        click(By.xpath("//input[@value='Delete']"));
        driver.switchTo().alert().accept();
    }

    public void createContact(ContactData contact, boolean creation) {
        initContactCreation();
        fillContactForm(contact, creation);
        submitContactCreation();
        returnToContactPage();
    }
    public void modify(ContactData contact) {
        editContactForm(contact.getId());
        fillContactForm(contact, false);
        updateContactModification();
        returnToContactPage();
    }


    private void returnToContactPage() {
        click(By.linkText("home page"));
    }

    public boolean isThereAContact() {
        return isElementPresent(By.name("selected[]"));
    }

    public void delete(ContactData contact) {
        selectContactById(contact.getId());
        deleteContact();

    }
    public int getGroupCount() {
        return driver.findElements(By.name("selected[]")).size();
    }

    public Set<ContactData> all() {
        Set<ContactData> contacts = new HashSet<ContactData>();
        List<WebElement> elements = driver.findElements(By.name("entry"));
        for (WebElement element: elements) {
            List<WebElement> cells = element.findElements(By.tagName("td"));
            String lastname = cells.get(1).getText();
            String firstname = cells.get(2).getText();
            int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));
            contacts.add(new ContactData().withId(id).withFirstname(firstname).withLastname(lastname));
        }
        return contacts;
    }
}
