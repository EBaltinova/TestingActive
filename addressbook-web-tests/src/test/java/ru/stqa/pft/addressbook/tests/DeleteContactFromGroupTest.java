package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteContactFromGroupTest extends TestBase {

    public List<GroupData> groups;
    public GroupData group;
    public ContactData contact;


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

        groups = app.db().groups().stream()
                .filter(g -> g.getContacts().size() != 0)
                .collect(Collectors.toList());

        if (groups.size() == 0) {
            contact = app.db().contacts().iterator().next();
            group = app.db().groups().iterator().next();
            app.contact().addToGroup(contact, group);
        } else {
            group = groups.iterator().next();
            contact = group.getContacts().iterator().next();
        }
    }

    @Test
    public void deleteContactFromGroupTest() {
        ContactData contact = app.db().contacts().stream().filter((c) -> c.getGroups().size() != 0).findAny().get();
        GroupData group = contact.getGroups().iterator().next();

        app.contact().deleteFromGroup(contact, group);

        boolean contactNotExists = app.db().contacts().stream()
                .filter(c -> c.getId() == contact.getId())
                .anyMatch(c -> contact.getGroups().stream().anyMatch(g -> g.getId() == group.getId()));

        Assert.assertTrue(contactNotExists);
    }
}







