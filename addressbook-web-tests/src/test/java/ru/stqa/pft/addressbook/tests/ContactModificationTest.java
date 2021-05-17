package ru.stqa.pft.addressbook.tests;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import ru.stqa.pft.addressbook.appmanager.ContactHelper;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactModificationTest extends TestBase {

    private Iterator<Object[]> readJson(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String json = "";
            String line = reader.readLine();
            while (line != null) {
                json += line;
                line = reader.readLine();
            }
            Gson gson = new Gson();
            List<ContactData> contacts = gson.fromJson(json, new TypeToken<List<ContactData>>() {
            }.getType());
            return contacts.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator();
        }
    }

    private Iterator<Object[]> readCsv(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(path)))) {
            StringBuilder csv = new StringBuilder();
            String line = reader.readLine();

            while (line != null) {
                csv.append(line);
                line = reader.readLine();
            }

            CsvMapper mapper = new CsvMapper();
            MappingIterator<ContactData> personIter = mapper.readerWithTypedSchemaFor(ContactData.class).readValues(String.valueOf(csv));
            List<ContactData> contacts = personIter.readAll();

            return contacts.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator();
        }
    }

    @DataProvider
    public Object[] validContactsFromCsv() throws IOException {
        ContactData contact = (ContactData)readCsv("src/test/resources/contact.csv").next()[0];
        ContactData contactModified = (ContactData)readCsv("src/test/resources/contactModified.csv").next()[0];
        HashMap<String, Object> map = new HashMap<>();
        map.put("create", contact);
        map.put("modify", contactModified);

        return Collections.singletonList(map).toArray();
    }

    @DataProvider
    public Object[] validContactsFromJson() throws IOException {
        ContactData contact = (ContactData) readJson("src/test/resources/contacts").next()[0];
        ContactData contactModified = (ContactData) readJson("src/test/resources/contactModified").next()[0];
        HashMap<String, Object> map = new HashMap<>();
        map.put("create", contact);
        map.put("modify", contactModified);

        return Collections.singletonList(map).toArray();
    }

    @Test (dataProvider = "validContactsFromCsv")
    public void testContactModificationFromCsv(HashMap<String, ContactData> contactsMap) {
        ContactData contact = contactsMap.get("create");
        ContactData contactModify = contactsMap.get("modify");
        app.contact().create(contact, true);
        Contacts before = app.db().contacts();
        ContactData modifiedContact = before.iterator().next();
        contactModify.withId(modifiedContact.getId());
        ContactHelper contactHelper = app.contact();
        contactHelper.editContactFormById(contactModify.getId());
        contactHelper.fillContactForm(contactModify, false);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(contactHelper.isClickable(By.cssSelector("input[type=submit]:nth-child(1)")), "Update not available");
        softAssert.assertTrue(contactHelper.isClickable(By.cssSelector("input[type=submit]:nth-child(2)")), "Update button is not available");
        softAssert.assertAll();

        contactHelper.updateContactModification();
        Contacts after = app.db().contacts();
        assertThat(after, equalTo(before.without(modifiedContact).withAdded(contactModify)));
        verifyContactListInUI();
    }

    @Test (dataProvider = "validContactsFromJson")
    public void testContactModification(HashMap<String, ContactData> contactsMap) {
        ContactData contact = contactsMap.get("create");
        ContactData contactModify = contactsMap.get("modify");
        app.contact().create(contact, true);
        Contacts before = app.db().contacts();
        ContactData modifiedContact = before.iterator().next();
        contactModify.withId(modifiedContact.getId());
        ContactHelper contactHelper = app.contact();
        contactHelper.editContactFormById(contactModify.getId());
        contactHelper.fillContactForm(contactModify, false);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(contactHelper.isClickable(By.cssSelector("input[type=submit]:nth-child(1)")), "Update not available");
        softAssert.assertTrue(contactHelper.isClickable(By.cssSelector("input[type=submit]:nth-child(2)")), "Update button is not available");
        softAssert.assertAll();

        contactHelper.updateContactModification();
        Contacts after = app.db().contacts();
        assertThat(after, equalTo(before.without(modifiedContact).withAdded(contactModify)));
        verifyContactListInUI();
    }
}


