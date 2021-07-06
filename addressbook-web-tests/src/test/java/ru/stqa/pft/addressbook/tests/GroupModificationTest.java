package ru.stqa.pft.addressbook.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.openqa.selenium.By;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import ru.stqa.pft.addressbook.appmanager.ContactHelper;
import ru.stqa.pft.addressbook.appmanager.GroupHelper;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

public class GroupModificationTest extends TestBase {

    @DataProvider
    public Object[] validGroups(ITestContext context) throws Exception {
        String format = context.getCurrentXmlTest().getAllParameters().getOrDefault("format", null);
        GroupData group;
        GroupData groupModified;
        switch (format) {
            case "csv":
                group = (GroupData) app.group().readCsv("src/test/resources/group.csv").next()[0];
                groupModified = (GroupData) app.group().readCsv("src/test/resources/groupModified.csv").next()[0];
                break;
            case "xml":
                group = (GroupData) app.group().readXml("src/test/resources/group.xml").next()[0];
                groupModified = (GroupData) app.group().readXml("src/test/resources/groupModified.xml").next()[0];
                break;
            case "json":
                group = (GroupData) app.group().readJson("src/test/resources/group.json").next()[0];
                groupModified = (GroupData) app.group().readJson("src/test/resources/groupModified.json").next()[0];
                break;
            default:
                throw new Exception("Задан неверный формат файла");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("create", group);
        map.put("modify", groupModified);

        return Collections.singletonList(map).toArray();
    }

    @Test(dataProvider = "validGroups")
    public void testGroupModification(HashMap<String, GroupData> groupsMap) {
        app.goTo().groupPage();
        Groups groups = app.db().groups();
        if (groups.size() == 0) {
            GroupData group = groupsMap.get("create");
            app.group().create(group);
        }
        Groups before = app.db().groups();
        GroupData groupForModified = groupsMap.get("modify");
        Random random = new Random();
        GroupData modifiedGroup = before.stream().skip(random.nextInt(before.size())).findFirst().get();
        groupForModified.withId(modifiedGroup.getId());
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

        app.group().selectGroupById(modifiedGroup.getId());
        app.group().initGroupModification();
        assertThat("Update button is not available", app.group().isClickable(By.name("update")));
        app.group().fillGroupForm(groupForModified);
        app.group().submitGroupModification();
        Groups after = app.db().groups();
        assertThat(after, equalTo(before.without(modifiedGroup).withAdded(groupForModified)));
        verifyGroupListInUI();
    }
}
