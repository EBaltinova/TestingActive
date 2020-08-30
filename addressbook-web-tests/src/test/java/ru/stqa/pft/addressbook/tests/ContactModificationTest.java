package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactModificationTest extends TestBase {

    @Test
    public void testContactModification() {
        int before = app.getContactHelper().getGroupCount();
        if (! app.getContactHelper().isThereAContact()) {
            app.getContactHelper().createContact(new ContactData("Bekki", null, null, null, "Test1"), true);
        }
        app.getContactHelper().editContactForm();
        app.getContactHelper().fillContactForm(new ContactData("Bekki", "Howard", "332211", "bekki@howard.com", null), false);
        app.getContactHelper().updateContactModification();
        app.getNavigationHelper().gotoHomePage();
        int after = app.getContactHelper().getGroupCount();
        Assert.assertEquals(after, before);
    }
}
