package org.vaadin.olli;

import java.util.ArrayList;
import java.util.List;

public class Person {
    // properties suitable for a person
    private String firstName;
    private String lastName;
    private String email;

    public Person(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public static List<Person> getPersons() {
        ArrayList<Person> persons = new ArrayList<>();
        persons.add(new Person("John", "Doe", "jd@invalid.com"));
        persons.add(new Person("Mary", "Moe", "test@invalid.com"));
        return persons;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    // sample data Person objects

    public void setEmail(String email) {
        this.email = email;
    }
}
