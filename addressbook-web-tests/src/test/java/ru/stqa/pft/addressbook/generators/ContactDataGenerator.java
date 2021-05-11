package ru.stqa.pft.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import ru.stqa.pft.addressbook.model.ContactData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ContactDataGenerator {

    @Parameter(names = "-c", description = "Contact count")
    public int count;

    @Parameter(names = "-f", description = "Target file")
    public String file;

    @Parameter(names = "-d", description = "Data format")
    public String format;

    public String fileName;

    public static void main(String[] args) throws IOException {
        ContactDataGenerator generator = new ContactDataGenerator();
        JCommander jCommander = new JCommander(generator);
        try {
            jCommander.parse(args);
        } catch (ParameterException ex) {
            jCommander.usage();
            return;
        }
        generator.run();
    }

    private void run() throws IOException {
        fileName = file + "." + format;
        List<ContactData> contacts = generateContacts(count);
        if (format.equals("csv")) {
            saveAsCsv(contacts, new File(fileName));
        } else if (format.equals("xml")) {
            saveAsXml(contacts, new File(fileName));
        } else if (format.equals("json")) {
            saveAsGson(contacts, new File(fileName));
        }
        else {
            System.out.println("Unrecognized format " + format);
        }
    }

    private void saveAsGson(List<ContactData> contacts, File fileName) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(contacts);
        try (Writer writer = new FileWriter(fileName)) {
            writer.write(json);
        }
    }

    private void saveAsXml(List<ContactData> contacts, File fileName) throws IOException {
        XStream xstream = new XStream();
        xstream.processAnnotations(ContactData.class);
        String xml = xstream.toXML(contacts);
        try (Writer writer = new FileWriter(fileName)) {
            writer.write(xml);
        }
    }

    private void saveAsCsv(List<ContactData> contacts, File fileName) throws IOException {
        System.out.println(new File(".").getAbsolutePath());
        try (Writer writer = new FileWriter(fileName)) {
            for (ContactData contact : contacts) {
                writer.write(String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s\n", contact.getFirstname(), contact.getLastname(),
                        contact.getNickname(), contact.getAddress(), contact.getFirstEmail(),contact.getSecondEmail(),
                        contact.getThirdEmail(), contact.getHomePhone(), contact.getHomeSecPhone(), contact.getMobilePhone(), contact.getWorkPhone()));
            }
        }
    }
    private List<ContactData> generateContacts(int count) {
        List<ContactData> contacts = new ArrayList<ContactData>();
        for (int i = 0; i < count; i++) {
            contacts.add(new ContactData().withFirstname(String.format("firstName %s", i))
                    .withLastname(String.format("lastName %s", i))
                    .withNickname(String.format("nickname %s", i))
                    .withHomePhone(String.format("111%s", i))
                    .withMobilePhone(String.format("222%s", i))
                    .withWorkPhone(String.format("333%s", i))
                    .withHomeSecPhone(String.format("444%s", i))
                    .withAddress(String.format("address %s", i))
                    .withFirstEmail(String.format("first@email%s", i))
                    .withSecondEmail(String.format("second@email%s", i))
                    .withThirdEmail(String.format("third@email%s", i)));

        }
        return contacts;
    }

}
