package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDeletionTest extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        if (! app.contact().isThereAContact()) {
            app.contact().create(new ContactData().withFirstname("Bekki"), true);
        }
    }

    @Test (enabled = false)
    public void testContactDeletion() {
        Contacts before = app.db().contacts();
        ContactData deletedContact = before.iterator().next();
        app.contact().delete(deletedContact);
        app.goTo().homePage();
        assertThat(app.contact().count(), equalTo(before.size()-1));
        Contacts after = app.db().contacts();
        assertThat(after, equalTo(before.without(deletedContact)));
    }

    @Test
    public void testContactGroupingDeletion() {
        Contacts before = app.db().contacts();
        List <Integer> ids = before.stream().map(contactData -> contactData.getId()).collect(Collectors.toList());
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            int idContact = ids.remove(random.nextInt(ids.size()));
            app.contact().selectContactById(idContact);
            ContactData contact = before.stream().filter(contactData -> contactData.getId()==idContact).findAny().get();
            before.remove(contact);
        }

        app.contact().deleteContact();
        app.goTo().homePage();
        assertThat(app.contact().count(), equalTo(before.size()));
        Contacts after = app.db().contacts();
        assertThat(after, equalTo(before));
    }

}