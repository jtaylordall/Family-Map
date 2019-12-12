package com.example.familymap.support;

import android.util.Log;

import com.example.familymap.data.DataStash;
import com.example.familymap.model.Event;
import com.example.familymap.model.Person;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.Vector;

public class Filter {

    private FamilyFinder familyFinder;
    private DataStash dataStash;

    public Filter() {
        dataStash = DataStash.getInstance();
        familyFinder = new FamilyFinder();
    }

    public Event[] filterFathersSide(Event[] inEvents) {
        Vector<Event> filteredEvents = new Vector<>();
        SortedSet<String> ids = familyFinder.getFathersSideIds();
        Log.d("FATHER-SIDE IDS", "->" + ids.size());
        for (Event e : inEvents) {
            if (ids.contains(e.getPersonID())) {
                filteredEvents.add(e);
            }
        }
        filteredEvents.addAll(getSpouseEvents(dataStash.getActivePerson()));
        return arrayFromVectorE(filteredEvents);
    }


    private Vector<Event> getSpouseEvents(Person person) {
        Vector<Event> filteredEvents = new Vector<>();
        Person spouse = familyFinder.findPerson(person.getSpouseID());
        if (new Search().personPassesFilters(spouse)) {
            Event[] spouseEvents = new EventFinder().getPersonEvents(spouse.getPersonID());
            filteredEvents.addAll(Arrays.asList(spouseEvents));
        }
        return filteredEvents;
    }

    public Event[] filterMothersSide(Event[] inEvents) {
        Vector<Event> filteredEvents = new Vector<>();
        SortedSet<String> ids = familyFinder.getMothersSideIds();
        Log.d("MOTHER-SIDE IDS", "->" + ids.size());
        for (Event e : inEvents) {
            if (ids.contains(e.getPersonID())) {
                filteredEvents.add(e);
            }
        }
        filteredEvents.addAll(getSpouseEvents(dataStash.getActivePerson()));
        return arrayFromVectorE(filteredEvents);
    }

    public Event[] filterMales(Event[] inEvents) {
        Vector<Event> filteredEvents = new Vector<>();
        for (Event e : inEvents) {
            Person p = new FamilyFinder().findPerson(e.getPersonID());
            if ("m".equals(p.getGender())) {
                filteredEvents.add(e);
            }
        }
        return arrayFromVectorE(filteredEvents);
    }

    public Event[] filterFemales(Event[] inEvents) {
        Vector<Event> filteredEvents = new Vector<>();
        for (Event e : inEvents) {
            Person p = new FamilyFinder().findPerson(e.getPersonID());
            if ("f".equals(p.getGender())) {
                filteredEvents.add(e);
            }
        }
        return arrayFromVectorE(filteredEvents);
    }

    public Event[] arrayFromVectorE(Vector<Event> v) {
        Event[] out = new Event[v.size()];
        for (int a = 0; a < out.length; a++) {
            out[a] = v.elementAt(a);
        }
        return out;
    }

    public Person[] arrayFromVectorP(Vector<Person> v) {
        Person[] out = new Person[v.size()];
        for (int a = 0; a < out.length; a++) {
            out[a] = v.elementAt(a);
        }
        return out;
    }

}
