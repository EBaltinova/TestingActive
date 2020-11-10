package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class DeleteContactFromGroupTest extends TestBase {

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
    public void deleteContactFromGroup() {
        ContactData contact = app.db().contacts().iterator().next();
        List <GroupData> contactGroups = new ArrayList<>(contact.getGroups());
        if (contactGroups.size() != 0) {
            GroupData group = contactGroups.iterator().next();
            app.contact().deleteFromGroup(contact, group);
        }
        else {
            GroupData group = app.db().groups().iterator().next();
            app.contact().addToGroup(contact, group);
            app.contact().deleteFromGroup(contact, group);
        }
            List<ContactData> contacts = new ArrayList<>(app.db().contacts());
            boolean contactInGroupNotExists = false;
            for (int index = 0; index < contacts.size(); index++) {
                if (contacts.get(index).getId() == contact.getId()) {
                    for (int indexGroup = 0; indexGroup < contactGroups.size(); indexGroup++) {
                        if (contactGroups.get(indexGroup).getId() != group.getId()) {
                            contactInGroupNotExists = true;
                        }
                    }
                }
            }
            Assert.assertTrue(contactInGroupNotExists);
        }
    }
}


