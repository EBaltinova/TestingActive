package ru.stqa.pft.addressbook.tests;// Generated by Selenium IDE

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTest extends TestBase {

    @Test
    public void testContactCreation() {
        Contacts before = app.contact().all();
        File photo = new File("src/test/resources/girl.png");
        ContactData contact = new ContactData().withFirstname("Bekki")
                .withLastname("Checker").withGroup("test2").withPhoto(photo);
        app.contact().createContact((contact), true);
        app.goTo().gotoHomePage();
        assertThat(app.contact().count(), equalTo(before.size() + 1));
        Contacts after = app.contact().all();
        assertThat(after, equalTo(
                before.withAdded(contact.withId(after.stream().mapToInt((c) -> c.getId()).max().getAsInt()))));
    }

    @Test
    public void testCurrentDir(){
        File currentDir = new File(".");
        System.out.println(currentDir.getAbsolutePath());
        File photo = new File("src/test/resources/girl.png");
        System.out.println(photo.getAbsolutePath());
        System.out.println(photo.exists());

    }
}
