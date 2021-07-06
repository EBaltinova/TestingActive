package ru.stqa.pft.addressbook.appmanager;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.xstream.XStream;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.acl.Group;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class GroupHelper extends HelperBase {

    public GroupHelper(WebDriver driver) {
        super(driver);
    }

    public void returnToGroupPage() {
        click(By.linkText("groups"));
    }

    public void submitGroupCreation() {
      click(By.name("submit"));
    }

    public void fillGroupForm(GroupData groupData) {
        type(By.name("group_name"), groupData.getName());
        type(By.name("group_header"), groupData.getHeader());
        type(By.name("group_footer"), groupData.getFooter());
    }

    public void initGroupCreation() {
        click(By.name("new"));
    }

    public void deleteSelectedGroups() {
        click(By.name("delete"));
    }

    public void selectGroupById(int id) {
        driver.findElement(By.cssSelector("input[value='" + id + "']")).click();
    }
    public void initGroupModification() {
        click(By.name("edit"));
    }

    public void submitGroupModification() {
        click(By.name("update"));
    }

    public void create(GroupData group) {
        initGroupCreation();
        fillGroupForm(group);
        submitGroupCreation();
        returnToGroupPage();
        groupCache = null;
    }

    public void modify(GroupData group) {
        selectGroupById(group.getId());
        initGroupModification();
        fillGroupForm(group);
        submitGroupModification();
        returnToGroupPage();
        groupCache = null;
    }

    public void delete(GroupData group) {
        selectGroupById(group.getId());
        deleteSelectedGroups();
        returnToGroupPage();
        groupCache = null;
    }

    public int count() {
        return driver.findElements(By.name("selected[]")).size();
    }

    private Groups groupCache = null;

    public Groups all() {
        if (groupCache != null) {
            return new Groups(groupCache);
        }
        groupCache = new Groups();
        List<WebElement> elements = driver.findElements(By.cssSelector("span.group"));
        for (WebElement element: elements) {
            String name = element.getText();
            int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));
            groupCache.add(new GroupData().withId(id).withName(name));
        }
        return new Groups(groupCache);
    }

    public List<GroupData> validGroupsFromXml(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(path)))) {
            StringBuilder xml = new StringBuilder();
            String line = reader.readLine();

            while (line != null) {
                xml.append(line);
                line = reader.readLine();
            }

            XStream xstream = new XStream();
            xstream.processAnnotations(GroupData.class);

            return (List<GroupData>) xstream.fromXML(xml.toString());
        }
    }

    public List<GroupData> validGroupsFromCsv(String path) throws IOException {
        CsvMapper mapper = new CsvMapper();
        MappingIterator<GroupData> personIter = mapper.readerWithTypedSchemaFor(GroupData.class).readValues(new FileReader(path));

        return personIter.readAll();
    }

    public List<GroupData> validGroupsFromJson(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            StringBuilder json = new StringBuilder();
            String line = reader.readLine();

            while (line != null) {
                json.append(line);
                line = reader.readLine();
            }

            Gson gson = new Gson();

            return gson.fromJson(json.toString(), new TypeToken<List<GroupData>>() {
            }.getType());
        }
    }
    public Iterator<Object[]> readJson(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String json = "";
            String line = reader.readLine();
            while (line != null) {
                json += line;
                line = reader.readLine();
            }
            Gson gson = new Gson();
            List<GroupData> groups = gson.fromJson(json, new TypeToken<List<GroupData>>() {
            }.getType());
            return groups.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator();
        }
    }
    public Iterator<Object[]> readCsv(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(path)))) {
            StringBuilder csv = new StringBuilder();
            String line = reader.readLine();

            while (line != null) {
                csv.append(line);
                line = reader.readLine();
            }

            CsvMapper mapper = new CsvMapper();
            MappingIterator<GroupData> personIter = mapper.readerWithTypedSchemaFor(GroupData.class).readValues(String.valueOf(csv));
            List<GroupData> groups = personIter.readAll();

            return groups.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator();
        }
    }

    public Iterator<Object[]> readXml(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            StringBuilder xml = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                xml.append(line);
                line = reader.readLine();
            }
            XStream xstream = new XStream();
            xstream.processAnnotations(GroupData.class);
            List<GroupData> groups = (List<GroupData>) xstream.fromXML(xml.toString());
            return groups.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator();
        }
    }
}
