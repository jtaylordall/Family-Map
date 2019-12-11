package com.example.familymap.data;

import com.example.familymap.model.AuthToken;
import com.example.familymap.model.EventWrapper;
import com.example.familymap.model.Person;
import com.example.familymap.model.PersonWrapper;

import java.util.Map;
import java.util.TreeMap;

public class DataStash {

    private static DataStash single_instance = null;

    private String host;
    private String port;
    private AuthToken authToken;
    private PersonWrapper personWrapper;
    private Map<String, Person> personMap;
    private EventWrapper eventWrapper;
    private boolean loggedIn;
    private Map<String, Integer> eventColors;
    private Person activePerson;

    private DataStash() {
        loggedIn = false;
        personMap = new TreeMap<>();
        eventColors = new TreeMap<>();
    }

    public static DataStash getInstance() {
        if (single_instance == null) {
            single_instance = new DataStash();
        }
        return single_instance;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public Person getActivePerson() {
        return activePerson;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public PersonWrapper getPersonWrapper() {
        return personWrapper;
    }

    public Map<String, Person> getPersonMap() {
        return personMap;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public EventWrapper getEventWrapper() {
        return eventWrapper;
    }

    public Map<String, Integer> getEventColors() {
        return eventColors;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setPersonWrapper(PersonWrapper personWrapper) {
        this.personWrapper = personWrapper;
        Map<String, Person> personMap = new TreeMap<>();
        Person[] persons = personWrapper.getPeople();
        for (Person p : persons) {
            personMap.put(p.getPersonID(), p);
        }
        setPersonMap(personMap);
    }

    public void setPersonMap(Map<String, Person> personMap) {
        this.personMap = personMap;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public void setEventWrapper(EventWrapper eventWrapper) {
        this.eventWrapper = eventWrapper;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setEventColors(Map<String, Integer> eventColors) {
        this.eventColors = eventColors;
    }

    public void setActivePerson(Person activePerson) {
        this.activePerson = activePerson;
    }

    public int getEventColor(String eventType) {
        if (eventColors.containsKey(eventType)) {
            return eventColors.get(eventType);
        }
        return 0;
    }

}
