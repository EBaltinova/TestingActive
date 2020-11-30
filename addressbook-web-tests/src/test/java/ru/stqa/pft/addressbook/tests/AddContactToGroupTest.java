package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddContactToGroupTest extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        if (app.db().groups().size() == 0) {
            app.goTo().groupPage();
            app.group().create(new GroupData().withName("Test").withHeader("TestHeader").withFooter("TestFooter"));
        }
        if (app.db().contacts().size() == 0) {
            app.goTo().homePage();
            app.contact().create(new ContactData().withFirstname("Bekki"), true);
        }
    }

    @Test
    public void addContactToGroup() {
       ContactData contact = app.db().contacts().stream()
               .filter((c) -> c.getGroups().size() == 0).findFirst().get();
        GroupData group = app.db().groups().iterator().next();
        app.contact().addToGroup(contact, group);

        boolean contactExists = app.db().contacts().stream()
                .filter(c -> c.getId() == contact.getId())
                .anyMatch(c -> c.getGroups().stream().anyMatch(g -> g.getId() == group.getId()));

        Assert.assertTrue(contactExists);

//        boolean contactExists = false;
//        for (int index = 0; index < contacts.size(); index++) {
//            if (contacts.get(index).getId() == contact.getId()) {
//                List<GroupData> contactGroups = new ArrayList<>(contact.getGroups());
//                for (int indexGroup = 0; indexGroup < contactGroups.size(); indexGroup++) {
//                    if (contactGroups.get(indexGroup).getId() == group.getId()) {
//                        contactExists = true;
//                    }
//                }
//            }
//        }
//        Assert.assertTrue(contactExists);
   }
}
