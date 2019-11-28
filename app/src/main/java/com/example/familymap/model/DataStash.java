package com.example.familymap.model;

public class DataStash {

    private static DataStash single_instance = null;

    private String host;
    private String port;
    private Person person;
    private AuthToken authToken;
    private People people;
    private Events events;
    private boolean loggedIn;

    public DataStash(){
        loggedIn = false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public static DataStash getInstance() {
        if(single_instance == null){
            single_instance = new DataStash();
        }
        return single_instance;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public People getPeople() {
        return people;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public Person getPerson() {
        return person;
    }

    public Events getEvents() {
        return events;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setPeople(People people) {
        this.people = people;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setEvents(Events events) {
        this.events = events;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
