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
        type(By.name("nickname"), contactData.getNickname());
        type(By.name("middlename"), contactData.getMiddleName());
        type(By.name("title"), contactData.getTitle());
        type(By.name("address"), contactData.getAddress());
        type(By.name("company"), contactData.getCompany());
        type(By.name("fax"), contactData.getFaxPhone());
        type(By.name("homepage"), contactData.getHomepage());
        type(By.name("address2"), contactData.getSecondAddress());

        type(By.name("bday"), String.format("%d", contactData.getBday()));
        type(By.name("bmonth"), contactData.getBmonth());
        type(By.name("byear"), contactData.getByear());
        type(By.name("aday"), String.format("%d", contactData.getAday()));
        type(By.name("amonth"), contactData.getAmonth());
        type(By.name("ayear"), contactData.getAyear());

        type(By.name("notes"), contactData.getNotes());
        type(By.name("home"), contactData.getHomePhone());
        type(By.name("work"), contactData.getWorkPhone());
        type(By.name("mobile"), contactData.getMobilePhone());
        type(By.name("phone2"), contactData.getHomeSecPhone());
        type(By.name("email"), contactData.getFirstEmail());
        type(By.name("email2"), contactData.getSecondEmail());
        type(By.name("email3"), contactData.getThirdEmail());
        attach(By.name("photo"),contactData.getPhoto());


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
        isAlertPresent();
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
        String nickname = driver.findElement(By.name("nickname")).getAttribute("value");
        String middlename = driver.findElement(By.name("middlename")).getAttribute("value");
        String title = driver.findElement(By.name("title")).getAttribute("value");
        String company = driver.findElement(By.name("company")).getAttribute("value");
        String faxPhone = driver.findElement(By.name("faxPhone")).getAttribute("value");
        String homepage = driver.findElement(By.name("homepage")).getAttribute("value");
        String secondAddress = driver.findElement(By.name("secondAddress")).getAttribute("value");
        String notes = driver.findElement(By.name("notes")).getAttribute("value");
        Integer bday = Integer.parseInt(driver.findElement(By.name("bday")).getAttribute("value"));
        String bmonth = driver.findElement(By.name("bmonth")).getAttribute("value");
        String byear = driver.findElement(By.name("byear")).getAttribute("value");
        Integer aday = Integer.parseInt(driver.findElement(By.name("aday")).getAttribute("value"));
        String amonth = driver.findElement(By.name("amonth")).getAttribute("value");
        String ayear = driver.findElement(By.name("ayear")).getAttribute("value");
        String home = driver.findElement(By.name("home")).getAttribute("value");
        String mobile = driver.findElement(By.name("mobile")).getAttribute("value");
        String work = driver.findElement(By.name("work")).getAttribute("value");
        String homeSec = driver.findElement(By.name("phone2")).getAttribute("value");
        String firstEmail = driver.findElement(By.name("email")).getAttribute("value");
        String secondEmail = driver.findElement(By.name("email2")).getAttribute("value");
        String thirdEmail = driver.findElement(By.name("email3")).getAttribute("value");
        String address = driver.findElement(By.name("address")).getAttribute("value");
        driver.navigate().back();
        return new ContactData().withId(contact.getId()).withFirstname(firstname).withLastname(lastname).withNickname(nickname)
                .withTitle(title).withCompany(company).withFaxPhone(faxPhone).withHomepage(homepage).withSecondAddress(secondAddress)
                .withNotes(notes).withHomePhone(home).withMiddleName(middlename)
                .withMobilePhone(mobile).withWorkPhone(work).withHomeSecPhone(homeSec)
                .withBday(bday).withBmonth(bmonth).withByear(byear)
                .withAday(aday).withAmonth(amonth).withAyear(ayear)
                .withFirstEmail(firstEmail).withSecondEmail(secondEmail).withThirdEmail(thirdEmail).withAddress(address);

    }

    private void initContactModificationById(int id) {
        driver.findElement(By.cssSelector(String.format("a[href='edit.php?id=%s']",id))).click();
    }

}
