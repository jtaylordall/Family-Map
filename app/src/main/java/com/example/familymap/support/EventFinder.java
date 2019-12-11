package com.example.familymap.support;

import com.example.familymap.data.DataStash;
import com.example.familymap.model.Event;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

public class EventFinder {
    private DataStash dataStash;

    public EventFinder() {
        dataStash = DataStash.getInstance();
    }

    public Event[] getPersonEvents(String personID) {
        Event[] allEvents = dataStash.getEventWrapper().getEvents();
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
        return sortEvents(personEventsArray);
    }

    public Event findEvent(String eventID) {
        Event[] allEvents = dataStash.getEventWrapper().getEvents();
        Event event = null;
        if (eventID != null && !"".equals(eventID)) {
            for (Event e : allEvents) {
                if (eventID.equals(e.getEventID())) {
                    event = e;
                }
            }
        }
        return event;
    }

    private Event[] sortEvents(Event[] inEvents) {
        Event birth = null;
        Event death = null;
        SortedSet<Event> otherEvents = new TreeSet<>();

        if (inEvents.length > 0) {
            for (Event e : inEvents) {
                if ("birth".equals(e.getEventType())) {
                    birth = e;
                } else if ("death".equals(e.getEventType())) {
                    death = e;
                } else {
                    otherEvents.add(e);
                }
            }

            int eventSize = otherEvents.size();
            int start = 0;
            int end = eventSize;
            if (birth != null) {
                eventSize++;
                start++;
                end++;
            }
            if (death != null) {
                eventSize++;
            }
            Event[] outEvents = new Event[eventSize];
            if (birth != null && outEvents.length > 0) {
                outEvents[0] = birth;
            }
            if (death != null && outEvents.length > 0) {
                outEvents[outEvents.length - 1] = death;
            }
            Iterator i = otherEvents.iterator();
            for (int a = start; a < end; a++) {
                if(i.hasNext()) {
                    outEvents[a] = (Event) i.next();
                }
            }
            return outEvents;

        } else {
            return inEvents;
        }
    }
}
