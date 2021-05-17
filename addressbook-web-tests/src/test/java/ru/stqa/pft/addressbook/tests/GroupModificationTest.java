package ru.stqa.pft.addressbook.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

public class GroupModificationTest extends TestBase {

    @DataProvider
    public Iterator<Object[]> validGroupsFromJson() throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/groups.json")))) {
            String json = "";
            String line = reader.readLine();
            while (line != null) {
                json += line;
                line = reader.readLine();
            }
            Gson gson = new Gson();
            List<GroupData> groups = gson.fromJson(json, new TypeToken<List<GroupData>>() {
            }.getType()); //List<GroupData>.class
            return groups.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator();
        }
    }

    @Test(dataProvider = "validGroupsFromJson")
    public void testGroupModification(GroupData group) {
        app.goTo().groupPage();
        app.group().create(group);
        Groups before = app.db().groups();
        GroupData modifiedGroup = before.iterator().next();
        GroupData groupModify = new GroupData()
                .withId(modifiedGroup.getId()).withName("TestMod").withHeader("TestMod2").withFooter("TestMod3");
        app.goTo().groupPage();

        SoftAssert softAssert = new SoftAssert();
        assertThat("Add new contact button is not available", app.group().isClickable(By.linkText("add new")));

        Arrays
                .asList("new", "delete", "edit")
                .forEach((String elementName) -> {
                    String message = String.format("Element <%s> is not available", elementName);
                    softAssert.assertTrue(app.group().isClickable(By.name(elementName)), message);
                });
        softAssert.assertAll();

        app.group().selectGroupById(groupModify.getId());
        app.group().initGroupModification();
        assertThat("Update button is not available", app.group().isClickable(By.name("update")));
        app.group().fillGroupForm(groupModify);
        app.group().submitGroupModification();
        Groups after = app.db().groups();
        assertThat(after, equalTo(before.without(modifiedGroup).withAdded(groupModify)));
        verifyGroupListInUI();
    }
}
