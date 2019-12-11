package com.example.familymap.model;

import android.os.Build;

public class Event implements Comparable<Event> {
    private String associatedUsername;
    private String eventID;
    private String personID;
    private double latitude;
    private double longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;

    public Event(String associatedUsername, String eventID, String personID, double latitude, double longitude,
                 String country, String city, String eventType, int year) {
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getUserName() {
        return associatedUsername;
    }

    public void setUserName(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Event e = (Event) o;
        return associatedUsername.equals(e.getUserName()) &&
                eventID.equals(e.getEventID()) &&
                personID.equals(e.getPersonID()) &&
                latitude == e.getLatitude() &&
                longitude == e.getLongitude() &&
                country.equals(e.getCountry()) &&
                city.equals(e.getCity()) &&
                eventType.equals(e.getEventType()) &&
                year == e.getYear();
    }

    @Override
    public String toString() {
        return "\nassociatedUsername: " + associatedUsername +
                "\neventID: " + eventID +
                "\npersonID: " + personID +
                "\nlatitude: " + latitude +
                "\nlongitude: " + longitude +
                "\ncountry: " + country +
                "\ncity: " + city +
                "\neventType: " + eventType +
                "\nyear: " + year;
    }

    @Override
    public int compareTo(Event o) {
        if (year != o.getYear()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return Integer.compare(year, o.getYear());
            }
        } else if (!eventType.equals(o.getEventType())) {
            return eventType.compareTo(o.getEventType());
        }
        return 0;
    }
}