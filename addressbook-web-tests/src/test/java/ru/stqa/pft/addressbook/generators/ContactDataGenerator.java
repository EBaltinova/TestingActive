package ru.stqa.pft.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.RandomStringUtils;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

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
    private List<ContactData> generateContacts(int count) throws IOException {
        List<ContactData> contacts = new ArrayList<ContactData>();

        for (int i = 0; i < count; i++) {
            contacts.add(new ContactData().withFirstname(generateString(10,25))
                    .withLastname(generateString(10,25))
                    .withNickname(generateString(10,25))
                    .withMiddleName(generateString(10,25))
                    .withTitle(generateString(10,25))
                    .withCompany(generateString(10,25))
                    .withFaxPhone(generateInt())
                    .withHomepage(generateString(10,25))
                    .withSecondAddress(generateString(10,25))
                    .withNotes(generateString(10,25))
                    .withBday(Byte.parseByte("12"))
                    .withBmonth(String.format("May"))
                    .withByear(String.format("1999"))
                    .withAday(Byte.parseByte("12"))
                    .withAmonth(String.format("December"))
                    .withAyear(String.format("1993"))
                    .withHomePhone(generateInt())
                    .withMobilePhone(generateInt())
                    .withWorkPhone(generateInt())
                    .withHomeSecPhone(generateInt())
                    .withAddress(generateString(10,25))
                    .withFirstEmail(generateString(10,25))
                    .withSecondEmail(generateString(10,25))
                    .withPhoto(new File(randomFile("src/test/resources/PhotoForContacts")))
                    .withThirdEmail(generateString(10,25)));
        }
        return contacts;
    }

    private String generateString(int min, int max) {
        return RandomStringUtils.randomAlphanumeric(new Random().nextInt(max - min) + min);
    }

    private String generateInt() {
        return RandomStringUtils.randomNumeric(8);
    }

    private String randomFile(String path) throws IOException {
        Stream<Path> files = Files.walk(Paths.get(path)).filter(Files::isRegularFile);
        return files.skip(new Random().nextInt((int)files.count())).findFirst().get().toString();
    }

}
