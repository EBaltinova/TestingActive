package ru.stqa.pft.addressbook.model;

public class ContactData {
    private final String firstname;
    private final String lastname;
    private final String number_home;
    private final String email;
    private String group;

    public ContactData(String firstname, String lastname, String number_home, String email, String group) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.number_home = number_home;
        this.email = email;
        this.group = group;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getNumber_home() {
        return number_home;
    }

    public String getEmail() {
        return email;
    }

    public String getGroup() {
        return group;
    }
}
