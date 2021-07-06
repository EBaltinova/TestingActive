package ru.stqa.pft.addressbook.tests;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.xstream.XStream;
import org.openqa.selenium.By;
import org.testng.ITestContext;
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
    @DataProvider
    public Object[] validContacts(ITestContext context) throws Exception {
        String format = context.getCurrentXmlTest().getAllParameters().getOrDefault("format", null);
        ContactData contact;
        ContactData contactModified;
        switch (format) {
            case "csv":
                contact = (ContactData)app.contact().readCsv("src/test/resources/contact.csv").next()[0];
                contactModified = (ContactData)app.contact().readCsv("src/test/resources/contactModified.csv").next()[0];
                break;
            case "xml":
                contact = (ContactData) app.contact().readXml("src/test/resources/contact.xml").next()[0];
                contactModified = (ContactData) app.contact().readXml("src/test/resources/contactModified.xml").next()[0];
                break;
            case "json":
                contact = (ContactData) app.contact().readJson("src/test/resources/contact.json").next()[0];
                contactModified = (ContactData) app.contact().readJson("src/test/resources/contactModified.json").next()[0];
                break;
            default:
                throw new Exception("Задан неверный формат файла");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("create", contact);
        map.put("modify", contactModified);

        return Collections.singletonList(map).toArray();
    }

    @Test (dataProvider = "validContacts")
    public void testContactModification(HashMap<String, ContactData> contactsMap) {
        Contacts before = app.db().contacts();
        if (before.size() == 0) {
            ContactData contact = contactsMap.get("create");
            app.contact().create(contact, true);
        }
        ContactData contactModify = contactsMap.get("modify");
        Random random = new Random();
        ContactData modifiedContact = before.stream().skip(random.nextInt(before.size())).findFirst().get();
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


