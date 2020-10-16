package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import java.util.Arrays;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class ContactAddressTest extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        if (app.contact().all().size() == 0) {
            app.contact().createContact(new ContactData().withFirstname("Bekki").withGroup("Test1").withHomePhone("5646")
                    .withFirstEmail("bekki@gm.ru").withAddress("Mraksa, 55"), true);
        }
    }

    @Test
    public void testContactAddress() {
        app.goTo().gotoHomePage();
        ContactData contact = app.contact().all().iterator().next();
        ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact);
        assertThat(contact.getAddress(), equalTo(contactInfoFromEditForm));
    }

    /*private String mergeAddress(ContactData contact) {
        return Arrays.asList(contact.getAddress()
                .filter((s) -> ! s.equals(""))
                .map(ContactEmailTest::cleaned)
                .collect(Collectors.joining("\n"));
    } */

    //public static String cleaned (String emails){
    // return emails.replaceAll("\\s","").replaceAll("[-()]","");
    // } Примечание: для проверки электронной почты не требуется очищать поля от вырезаемых символов,
    // так как ничего не теряется при переходе из одного контакта ко всем контактам(???)


}
