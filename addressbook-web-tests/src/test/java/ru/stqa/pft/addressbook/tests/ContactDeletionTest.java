package ru.stqa.pft.addressbook.tests;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.appmanager.ContactHelper;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDeletionTest extends TestBase {
    @DataProvider
    public Iterator<Object[]> validContacts(ITestContext context) throws Exception {
        String format = context.getCurrentXmlTest().getAllParameters().getOrDefault("format", null);
        List<ContactData> contact;
        switch (format) {
            case "csv":
                contact = app.contact().validContactsFromCsv("src/test/resources/contact.csv");
                break;
            case "xml":
                contact = app.contact().validContactsFromXml("src/test/resources/contact.xml");
                break;
            case "json":
                contact = app.contact().validContactsFromJson("src/test/resources/contact.json");
                break;
            default:
                throw new Exception("Задан неверный формат файла");
        }

        return contact.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator();
    }

    @Test(enabled = false)
    public void testContactDeletion() {
        Contacts before = app.db().contacts();
        ContactData deletedContact = before.iterator().next();
        app.contact().delete(deletedContact);
        app.goTo().homePage();
        assertThat(app.contact().count(), equalTo(before.size() - 1));
        Contacts after = app.db().contacts();
        assertThat(after, equalTo(before.without(deletedContact)));
    }

    @Test(enabled = false)
    public void testContactGroupingDeletion() {
        Contacts before = app.db().contacts();
        List<Integer> ids = before.stream().map(contactData -> contactData.getId()).collect(Collectors.toList());
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            int idContact = ids.remove(random.nextInt(ids.size()));
            app.contact().selectContactById(idContact);
            ContactData contact = before.stream().filter(contactData -> contactData.getId() == idContact).findAny().get();
            before.remove(contact);
        }
        app.contact().deleteContact();
        app.goTo().homePage();
        assertThat(app.contact().count(), equalTo(before.size()));
        Contacts after = app.db().contacts();
        assertThat(after, equalTo(before));
    }

    @Test(dataProvider = "validContacts")
    public void testAllContactDeletion(ContactData contact) {
        Contacts before = app.db().contacts();
        ContactHelper contactHelper = app.contact();
        if (before.size() == 0) {
            contactHelper.initContactCreation();
            contactHelper.fillContactForm(contact, true);
            contactHelper.submitContactCreation();
        }
        app.contact().deleteAllContact();
        app.goTo().homePage();
        Contacts after = app.db().contacts();
        assertThat(app.contact().count(), equalTo(after.size()));
    }
}