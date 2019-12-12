package com.example.familymap.support;

import com.example.familymap.data.DataStash;
import com.example.familymap.data.SettingsManager;
import com.example.familymap.model.Event;
import com.example.familymap.model.Person;

import java.util.Vector;

public class Search {
    private DataStash dataStash;
    private Filter filter;
    private EventFinder eventFinder;
    private FamilyFinder familyFinder;
    private SettingsManager settingsManager;

    public Search() {
        dataStash = DataStash.getInstance();
        filter = new Filter();
        eventFinder = new EventFinder();
        familyFinder = new FamilyFinder();
        settingsManager = SettingsManager.getInstance();
    }

    public Object[] search(String input) {
        Person[] persons = searchPersons(input);
        Event[] events = searchEvents(input);
        Object[] output = new Object[persons.length + events.length];
        int i = 0;
        for (Person p : persons) {
            output[i] = p;
            i++;
        }
        for (Event e : events) {
            output[i] = e;
            i++;
        }
        return output;
    }

    public Person[] searchPersons(String input) {
        input = input.toLowerCase();
        Vector<Person> matches = new Vector<>();
        Person[] persons = dataStash.getPersonWrapper().getPeople();
        for (Person p : persons) {
            if (personPassesFilters(p)) {
                String firstName = p.getFirstName().toLowerCase();
                String lastName = p.getLastName().toLowerCase();
                if (firstName.contains(input) || lastName.contains(input)) {
                    matches.add(p);
                }
            }
        }
        return filter.arrayFromVectorP(matches);
    }

    public Event[] searchEvents(String input) {
        input = input.toLowerCase();
        Vector<Event> matches = new Vector<>();
        Event[] events = dataStash.getEventWrapper().getEvents();
        for (Event e : events) {
            if (eventPassesFilters(e)) {
                String eventType = e.getEventType().toLowerCase();
                String city = e.getCity().toLowerCase();
                String country = e.getCountry().toLowerCase();
                String year = String.valueOf(e.getYear());
                if (eventType.contains(input) ||
                        city.contains(input) ||
                        country.contains(input) ||
                        year.contains(input)) {
                    matches.add(e);
                }
            }
        }
        return filter.arrayFromVectorE(matches);
    }

    private boolean eventPassesFilters(Event event) {
        Person person = familyFinder.findPerson(event.getPersonID());
        return personPassesFilters(person);
    }

    private boolean personPassesFilters(Person person) {
        if (!settingsManager.isMaleEventsOn() && "m".equals(person.getGender())) {
            return false;
        }
        if (!settingsManager.isFemaleEventsOn() && "f".equals(person.getGender())) {
            return false;
        }
        if (immuneFatherMotherFilter(person)) {
            return true;
        }
        if (!settingsManager.isFathersSideOn()) {
            if (isFather(person)) {
                return false;
            }
            if (familyFinder.getFathersSideIds().contains(person.getPersonID())) {
                return false;
            }
        }
        if (!settingsManager.isMothersSideOn()) {
            if (isMother(person)) {
                return false;
            }
            if (familyFinder.getMothersSideIds().contains(person.getPersonID())) {
                return false;
            }
        }
        return true;
    }

    private boolean immuneFatherMotherFilter(Person person) {
        if (person.equals(dataStash.getActivePerson())) {
            return true;
        } else if (familyFinder.findPerson(person.getSpouseID())
                .equals(familyFinder
                        .findPerson(dataStash.getActivePerson()
                                .getSpouseID()))) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isFather(Person person) {
        if (person.equals(familyFinder.findPerson(
                dataStash.getActivePerson().getFatherID()))) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isMother(Person person) {
        if (person.equals(familyFinder.findPerson(
                dataStash.getActivePerson().getMotherID()))) {
            return true;
        } else {
            return false;
        }
    }

}
