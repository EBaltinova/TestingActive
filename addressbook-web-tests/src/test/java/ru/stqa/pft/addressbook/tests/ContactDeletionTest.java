package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

public class ContactDeletionTest extends TestBase {

    @Test
    public void testContactDeletion() {
        if (! app.getContactHelper().isThereAContact()) {
            app.getContactHelper().createContact(new ContactData("Bekki", null, null, null, "Test1"), true);
        }
        app.getContactHelper().chooseContact();
        app.getContactHelper().deleteContact();
        app.getNavigationHelper().gotoHomePage();

    }
}
