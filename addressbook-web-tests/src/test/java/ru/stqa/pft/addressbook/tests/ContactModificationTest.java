package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ContactModificationTest extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        if (app.contact().all().size() == 0) {
            app.contact().createContact(new ContactData().withFirstname("Bekki").withGroup("Test1"), true);
        }
    }

    @Test
    public void testContactModification() {
        Set<ContactData> before = app.contact().all();
        ContactData modifiedGroup = before.iterator().next();
        ContactData contact = new ContactData().
                withId(modifiedGroup.getId()).withFirstname("Bekki").withLastname("Howard").withNumber_home("332211").withEmail("bekki@howard.com");
        app.contact().modify(contact);
        Set<ContactData> after = app.contact().all();
        Assert.assertEquals(after.size(), before.size());
        before.remove(modifiedGroup);
        before.add(contact);
        Assert.assertEquals(before, after);

    }
}


