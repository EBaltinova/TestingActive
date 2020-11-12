package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.List;

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
        type(By.name("home"), contactData.getHomePhone());
        type(By.name("work"), contactData.getWorkPhone());
        type(By.name("mobile"), contactData.getMobilePhone());
        type(By.name("phone2"), contactData.getHomeSecPhone());
        type(By.name("email"), contactData.getFirstEmail());
        type(By.name("email2"), contactData.getSecondEmail());
        type(By.name("email3"), contactData.getThirdEmail());
        type(By.name("address"), contactData.getAddress());
        //attach(By.name("photo"),contactData.getPhoto());

        if (creation) {
            if (contactData.getGroups().size() > 0) {
                Assert.assertTrue(contactData.getGroups().size() == 1);
                new Select(driver.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroups().iterator().next().getName());
            }
            } else {
            Assert.assertFalse(isElementPresent(By.name("new_group")));
        }
    }

    public void initContactCreation() {
      click(By.linkText("add new"));
    }

    public void addToGroup(ContactData contact, GroupData group) {
        selectContactById(contact.getId());
        selectGroup(group);
        addSelectGroup();
        goToContactList();
    }

    public void deleteFromGroup(ContactData contact, GroupData group) {
        selectGroupForDisplay(group);
        selectContactById(contact.getId());
        removeContactFromGroup();
        goToContactList();
    }

    public void goToContactList() {
        driver.findElement(By.xpath("//*[@id=\"nav\"]/ul/li[1]/a")).click();
    }
    private void removeContactFromGroup() {
        click(By.xpath("//*[@id=\"content\"]/form[2]/div[3]/input[@name='remove']"));
    }

    private void selectGroupForDisplay(GroupData group) {
        driver.findElement(By.xpath("//*[@id=\"right\"]/select/option[@value=" + group.getId() + "]")).click();
    }

    public void selectGroup(GroupData group) {
        driver.findElement(By.xpath("//*[@id=\"content\"]/form[2]/div[4]/select/option[@value=" + group.getId() + "]")).click();
    }

    public void addSelectGroup() {
        click(By.xpath("//input[@value='Add to']"));
    }

    public void editContactFormById(int id) {
        driver.findElement(By.cssSelector("a[href='edit.php?id=" + id + "']")).click();
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

    public void create(ContactData contact, boolean creation) {
        initContactCreation();
        fillContactForm(contact, creation);
        submitContactCreation();
        returnToContactPage();
        contactCache = null;
    }

    public void modify(ContactData contact) {
        editContactFormById(contact.getId());
        fillContactForm(contact, false);
        updateContactModification();
        returnToContactPage();
        contactCache = null;
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
        contactCache = null;

    }
    public int count() {
        return driver.findElements(By.name("selected[]")).size();
    }

    private Contacts contactCache = null;

    public Contacts all() {
        if (contactCache != null) {
            return new Contacts(contactCache);
        }
        contactCache = new Contacts();
        Contacts contacts = new Contacts();
        List<WebElement> elements = driver.findElements(By.name("entry"));
        for (WebElement element: elements) {
            List<WebElement> cells = element.findElements(By.tagName("td"));
            String lastname = cells.get(1).getText();
            String firstname = cells.get(2).getText();
            String address = cells.get(3).getText();
            String allPhones = cells.get(5).getText();
            String allEmails = cells.get(4).getText();
            int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));
            contactCache.add(new ContactData().withId(id).withFirstname(firstname).withLastname(lastname)
                    .withAllPhones(allPhones).withAllEmails(allEmails).withAddress(address));
        }
        return new Contacts(contactCache);
    }

    public ContactData infoFromEditForm(ContactData contact) {
        initContactModificationById(contact.getId());
        String firstname = driver.findElement(By.name("firstname")).getAttribute("value");
        String lastname = driver.findElement(By.name("lastname")).getAttribute("value");
        String home = driver.findElement(By.name("home")).getAttribute("value");
        String mobile = driver.findElement(By.name("mobile")).getAttribute("value");
        String work = driver.findElement(By.name("work")).getAttribute("value");
        String homeSec = driver.findElement(By.name("phone2")).getAttribute("value");
        String firstEmail = driver.findElement(By.name("email")).getAttribute("value");
        String secondEmail = driver.findElement(By.name("email2")).getAttribute("value");
        String thirdEmail = driver.findElement(By.name("email3")).getAttribute("value");
        String address = driver.findElement(By.name("address")).getAttribute("value");
        driver.navigate().back();
        return new ContactData().withId(contact.getId()).withFirstname(firstname).withLastname(lastname)
                .withHomePhone(home).withMobilePhone(mobile).withWorkPhone(work).withHomeSecPhone(homeSec)
                .withFirstEmail(firstEmail).withSecondEmail(secondEmail).withThirdEmail(thirdEmail).withAddress(address);

    }

    private void initContactModificationById(int id) {
        driver.findElement(By.cssSelector(String.format("a[href='edit.php?id=%s']",id))).click();
    }

}
