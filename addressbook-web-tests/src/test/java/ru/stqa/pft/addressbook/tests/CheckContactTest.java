package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import ru.stqa.pft.addressbook.appmanager.ContactHelper;
import ru.stqa.pft.addressbook.model.ContactData;
import sun.reflect.Reflection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class CheckContactTest extends TestBase {
    @Test()
    public void testContactModification() {
        ContactHelper contactHelper = app.contact();
        ContactData contact = app.db().contacts().stream().iterator().next();
        contactHelper.openContactFormById(contact.getId());
        String src = contactHelper.getPageSource();
        SoftAssert softAssert = new SoftAssert();
        Arrays.asList(contact.getFirstname(), contact.getLastname(), contact.getNickname(), contact.getMiddleName(),
                contact.getCompany(), contact.getTitle(), contact.getHomePhone(), contact.getMobilePhone(),
                contact.getWorkPhone(), contact.getFaxPhone(), contact.getFirstEmail(), contact.getSecondEmail(),
                contact.getThirdEmail(), contact.getHomepage(), contact.getAday().toString(), contact.getBday().toString(),
                contact.getAmonth(), contact.getBmonth(), contact.getAyear(), contact.getByear(), contact.getHomeSecPhone(),
                contact.getSecondAddress(), contact.getNotes()
        ).forEach(value -> softAssert.assertTrue(src.contains(value), String.format("Wrong value <%s>", value)));
        softAssert.assertAll();

    }
}
