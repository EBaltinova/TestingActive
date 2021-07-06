package ru.stqa.pft.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import net.bytebuddy.utility.RandomString;
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
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        contact.getFirstname(),
                        contact.getLastname(),
                        contact.getNickname(),
                        contact.getAddress(),
                        contact.getFirstEmail(),
                        contact.getSecondEmail(),
                        contact.getThirdEmail(),
                        contact.getHomePhone(),
                        contact.getHomeSecPhone(),
                        contact.getMobilePhone(),
                        contact.getWorkPhone(),
                        contact.getMiddleName(),
                        contact.getTitle(),
                        contact.getCompany(),
                        contact.getFaxPhone(),
                        contact.getHomepage(),
                        contact.getSecondAddress(),
                        contact.getNotes(),
                        contact.getPhoto().getPath(),
                        contact.getBday(),
                        contact.getBmonth(),
                        contact.getByear(),
                        contact.getAday(),
                        contact.getAmonth(),
                        contact.getAyear()));
            }
        }
    }
    private List<ContactData> generateContacts(int count) throws IOException {
        List<ContactData> contacts = new ArrayList<ContactData>();

        for (int i = 0; i < count; i++) {
            String firstName = generateString(10,25);
            String lastName = generateString(10,25);
            String nickname = generateString(10,25);
            String middleName = generateString(10,25);
            String title = generateString(25,50);
            String company = generateString(10,25);
            String faxPhone = generateInt(8);
            String homepage = generateString(10,25);
            String secondAddress = generateString(10,25);
            String notes = generateString(10,25);
            String homePhone = generateInt(8);
            String mobilePhone = generateInt(8);
            String workPhone = generateInt(8);
            String homeSecPhone = generateInt(8);
            String address = generateString(10,25);
            String firstEmail = generateString(10,25);
            String secondEmail = generateString(10,25);
            String thirdEmail = generateString(10,25);
            File photo = new File(randomFile("PhotoForContacts"));

            contacts.add(new ContactData()
                    .withFirstname(firstName)
                    .withLastname(lastName)
                    .withNickname(nickname)
                    .withMiddleName(middleName)
                    .withTitle(title)
                    .withCompany(company)
                    .withFaxPhone(faxPhone)
                    .withHomepage(homepage)
                    .withSecondAddress(secondAddress)
                    .withNotes(notes)
                    .withPhoto(photo)
                    .withBday(Byte.parseByte("12"))
                    .withBmonth(String.format("May"))
                    .withByear(String.format("1999"))
                    .withAday(Byte.parseByte("12"))
                    .withAmonth(String.format("December"))
                    .withAyear(String.format("1993"))
                    .withHomePhone(homePhone)
                    .withMobilePhone(mobilePhone)
                    .withWorkPhone(workPhone)
                    .withHomeSecPhone(homeSecPhone)
                    .withAddress(address)
                    .withFirstEmail(firstEmail)
                    .withSecondEmail(secondEmail)
                    .withThirdEmail(thirdEmail));
        }
        return contacts;
    }

    private String generateString(int min, int max) {
        return RandomStringUtils.randomAlphanumeric(new Random().nextInt(max - min) + min);
    }

    private String generateInt(int count) {
        return RandomStringUtils.randomNumeric(count);
    }

    private String randomFile(String path) throws IOException {
        Path parent = Paths.get(System.getProperty("user.dir")).getParent().getParent().getParent();
        Path filePath = Files.walk(Paths.get(path))
                .filter(Files::isRegularFile)
                .sorted((o1, o2) -> ThreadLocalRandom.current().nextInt(-1, 2))
                .findAny()
                .get()
                .toAbsolutePath();

        return parent.relativize(filePath).toString();
    }

}
