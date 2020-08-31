package ru.stqa.pft.addressbook.model;

import java.util.Objects;

public class ContactData {
    private final String firstname;
    private final String lastname;
    private final String number_home;
    private final String email;
    private String group;
    private final String id;

    public ContactData(String firstname, String lastname, String number_home, String email, String group) {
        this.id = null;
        this.firstname = firstname;
        this.lastname = lastname;
        this.number_home = number_home;
        this.email = email;
        this.group = group;
    }
    public ContactData(String id, String firstname, String lastname, String number_home, String email, String group) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.number_home = number_home;
        this.email = email;
        this.group = group;
    }

    public String getId() {
        return id;
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

    @Override
    public String toString() {
        return "ContactData{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactData that = (ContactData) o;
        return Objects.equals(firstname, that.firstname) &&
                Objects.equals(lastname, that.lastname) &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, id);
    }
}
