package com.example.familymap.model;

public class Person {
    private String associatedUsername;
    private String personID;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;

    public Person(String associatedUsername, String personID, String firstName, String lastName,
                  String gender, String fatherID, String motherID, String spouseID) {
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public Person(Person person) {
        this.associatedUsername = person.getUserName();
        this.personID = person.getPersonID();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.gender = person.getGender();
        this.fatherID = person.getFatherID();
        this.motherID = person.getMotherID();
        this.spouseID = person.getSpouseID();
    }

    public Person() {

    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setUserName(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    public String getPersonID() {
        return personID;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUserName() {
        return associatedUsername;
    }

    public String getFatherID() {
        return fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Person p = (Person) o;
        return personID.equals(p.personID) &&
                associatedUsername.equals(p.getUserName()) &&
                firstName.equals(p.getFirstName()) &&
                lastName.equals(p.getLastName()) &&
                gender.equals(p.getGender()) &&
                fatherID.equals(p.getFatherID()) &&
                motherID.equals(p.getMotherID()) &&
                spouseID.equals(p.getSpouseID());
    }

    @Override
    public String toString() {
        return "\"" + firstName +
                " " + lastName + "\"";
    }
}
