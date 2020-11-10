package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class ContactEmailTest extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        if (app.contact().all().size() == 0) {
            app.contact().create(new ContactData().withFirstname("Bekki")
                    //withGroup("Test1")
                    .withHomePhone("5646").withFirstEmail("bekki@gm.ru"), true);
        }
    }

    @Test
    public void testContactEmails() {
        app.goTo().homePage();
        ContactData contact = app.contact().all().iterator().next();
        ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact);
        assertThat(contact.getAllEmails(), equalTo(mergeEmails(contactInfoFromEditForm)));
    }

    private String mergeEmails(ContactData contact) {
        String allEmails = contact.getFirstEmail() + contact.getSecondEmail() + contact.getThirdEmail();
        return allEmails;

        //return Arrays.asList(contact.getFirstEmail(), contact.getSecondEmail(), contact.getThirdEmail());
                //.stream().filter((s) -> ! s.equals(""))
               // .map(ContactEmailTest::cleaned)
               // .collect(Collectors.joining("\n"));
    }

   // public static String cleaned (String emails){
       // return emails.replaceAll("\\s","");

}
