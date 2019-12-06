package com.example.familymap.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class DataStash {

    private static DataStash single_instance = null;
    final private int ISNT_FAMILY = 0;
    final private int IS_FATHER = 1;
    final private int IS_MOTHER = 2;
    final private int IS_SPOUSE = 3;
    final private int IS_CHILD = 4;

    private String host;
    private String port;
    private AuthToken authToken;
    private People people;
    private Map<String, Person> personMap;
    private Events events;
    private boolean loggedIn;
    private Map<String, Integer> eventColors;
    private Person activePerson;

    public DataStash() {
        loggedIn = false;
        personMap = new TreeMap<>();
        eventColors = new TreeMap<>();
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public static DataStash getInstance() {
        if (single_instance == null) {
            single_instance = new DataStash();
        }
        return single_instance;
    }

    public Person getActivePerson() {
        return activePerson;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public People getPeople() {
        return people;
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

    public Events getEvents() {
        return events;
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

    public void setPeople(People people) {
        this.people = people;
        Map<String, Person> personMap = new TreeMap<>();
        Person[] persons = people.getPeople();
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

    public void setEvents(Events events) {
        this.events = events;
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

    public Event[] getPersonEvents(String personID) {
        Event[] allEvents = getEvents().getEvents();
        Vector<Event> personEvents = new Vector<>();
        for (Event e : allEvents) {
            if (e.getPersonID().equals(personID)) {
                personEvents.add(e);
            }
        }
        Event[] personEventsArray = new Event[personEvents.size()];
        for (int a = 0; a < personEvents.size(); a++) {
            personEventsArray[a] = personEvents.elementAt(a);
        }
        return personEventsArray;
    }

    public Map<Integer, Person> getPersonFamily(String personID) {
        Map<Integer, Person> famMapForOrder = new TreeMap<>();
        if (!"".equals(personID)) {
            Person[] allPeople = getPeople().getPeople();
            Person person = null;
            if (personID.equals(activePerson.getPersonID())) {
                person = activePerson;
            } else {
                person = findPerson(personID);
            }
            int siblingCounter = 0;
            for (Person p : allPeople) {
                System.out.println(p.toString());
                int relationship = getRelationship(person, p);
                System.out.println(relationship);
                switch (relationship) {
                    case IS_FATHER:
                        famMapForOrder.put(IS_FATHER, p);
                        break;
                    case IS_MOTHER:
                        famMapForOrder.put(IS_MOTHER, p);
                        break;
                    case IS_SPOUSE:
                        famMapForOrder.put(IS_SPOUSE, p);
                        break;
                    case IS_CHILD:
                        famMapForOrder.put(IS_CHILD + siblingCounter, p);
                        siblingCounter++;
                        break;
                    default:
                        // p not related, do nothing
                }
                System.out.println();
            }
            System.out.println(famMapForOrder.toString());
        }
        return famMapForOrder;
    }

    private int getRelationship(Person person, Person relative) {
        String personID = person.getPersonID();
        String fatherID = person.getFatherID();
        String motherID = person.getMotherID();
        String spouseID = person.getSpouseID();

        String relativeID = relative.getPersonID();
        String relativeFatherID = relative.getFatherID();
        String relativeMotherID = relative.getMotherID();

        if (relativeID != null && personID != null && !relativeID.equals(personID)) {
            if (fatherID != null && // relative is father
                    !"".equals(fatherID) &&
                    relativeID.equals(fatherID)) {
                return IS_FATHER;
            } else if (motherID != null && // relative is mother
                    !"".equals(motherID) &&
                    relativeID.equals(motherID)) {
                return IS_MOTHER;
            } else if (spouseID != null && // relative is spouse
                    !"".equals(spouseID) &&
                    relativeID.equals(spouseID)) {
                return IS_SPOUSE;
            } else if (relativeFatherID != null && // relative is child (person is father)
                    relativeFatherID.equals(personID)) {
                return IS_CHILD;
            } else if (relativeMotherID != null && // relative is child (person is mother)
                    relativeMotherID.equals(personID)) {
                return IS_CHILD;
            }
        }
        return ISNT_FAMILY;
    }

    private Person findPerson(String personID) {
        Person[] allPeople = getPeople().getPeople();
        Person person = null;
        if (personID != null && !"".equals(personID)) {
            for (Person p : allPeople) {
                if (personID.equals(p.getPersonID())) {
                    person = p;
                }
            }
        }
        return person;
    }

    public int getEventColor(String eventType) {
        if (eventColors.containsKey(eventType)) {
            return eventColors.get(eventType);
        }
        return 0;
    }

    public Drawable getMarkerIcon(Context context, int color) {
        return new IconDrawable(context,
                FontAwesomeIcons.fa_map_marker).color(color).sizeDp(40);
    }
}
