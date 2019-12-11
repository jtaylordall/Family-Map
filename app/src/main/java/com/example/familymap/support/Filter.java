package com.example.familymap.support;

import android.util.Log;

import com.example.familymap.model.Event;
import com.example.familymap.model.Person;

import java.util.SortedSet;
import java.util.Vector;

public class Filter {

    private FamilyFinder familyFinder;

    public Filter(){
        familyFinder = new FamilyFinder();
    }

    public Event[] filterFathersSide(Event[] inEvents){
        Vector<Event> filteredEvents = new Vector<>();
        SortedSet<String> ids = familyFinder.getFathersSideIds();
        Log.d("FATHER-SIDE IDS", "->" + ids.size());
        for (Event e : inEvents){
            if (ids.contains(e.getPersonID())){
                filteredEvents.add(e);
            }
        }
        return arrayFromVector(filteredEvents);
    }

    public Event[] filterMothersSide(Event[] inEvents){
        Vector<Event> filteredEvents = new Vector<>();
        SortedSet<String> ids = familyFinder.getMothersSideIds();
        Log.d("MOTHER-SIDE IDS", "->" + ids.size());
        for (Event e : inEvents){
            if (ids.contains(e.getPersonID())){
                filteredEvents.add(e);
            }
        }
        return arrayFromVector(filteredEvents);
    }

    public Event[] filterMales(Event[] inEvents){
        Vector<Event> filteredEvents = new Vector<>();
        for (Event e : inEvents){
            Person p = new FamilyFinder().findPerson(e.getPersonID());
            if("m".equals(p.getGender())){
                filteredEvents.add(e);
            }
        }
        return arrayFromVector(filteredEvents);
    }
    public Event[] filterFemales(Event[] inEvents){
        Vector<Event> filteredEvents = new Vector<>();
        for (Event e : inEvents){
            Person p = new FamilyFinder().findPerson(e.getPersonID());
            if("f".equals(p.getGender())){
                filteredEvents.add(e);
            }
        }
        return arrayFromVector(filteredEvents);
    }

    private Event[] arrayFromVector(Vector<Event> v){
        Event[] out = new Event[v.size()];
        for (int a = 0; a < out.length; a++){
            out[a] = v.elementAt(a);
        }
        return out;
    }

}
