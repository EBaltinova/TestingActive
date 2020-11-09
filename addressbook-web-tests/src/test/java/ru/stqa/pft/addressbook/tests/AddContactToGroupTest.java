package ru.stqa.pft.addressbook.tests;

import org.hamcrest.MatcherAssert;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AddContactToGroupTest extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        if (app.db().contacts().size() == 0) {
            app.goTo().gotoHomePage();
            app.contact().create(new ContactData().withFirstname("Bekki"), true);
            //.withGroup("Test1"), true);
        }
    }

    @Test
    public void addContactToGroup() {
        ContactData contact = app.db().contacts().iterator().next();
        GroupData group = app.db().groups().iterator().next();
        app.contact().addToGroup(contact, group);
        List<ContactData> contacts = new ArrayList<>(app.db().contacts());

        //boolean contactExists = app.db().contacts().stream()
              //  .filter(c -> c.getId() == contact.getId())
           //     .anyMatch(c -> contact.getGroups().stream().anyMatch(g -> g.getId() == group.getId()));

       // Assert.assertTrue(contactExists);
        
        boolean contactExists = false;
        for (int index = 0; index < contacts.size(); index++) {
            if (contacts.get(index).getId() == contact.getId()) {
                List <GroupData> contactGroups = new ArrayList<>(contact.getGroups());
                for (int indexGroup = 0; indexGroup < contactGroups.size(); indexGroup++) {
                    if (contactGroups.get(indexGroup).getId() == group.getId()) {
                        contactExists = true;
                    }
                }
            }
        }
        Assert.assertTrue(contactExists);
    }
}
