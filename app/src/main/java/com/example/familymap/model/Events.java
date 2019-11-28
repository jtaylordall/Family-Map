package com.example.familymap.model;

public class Events {
    private Event[] data;

    public Events(){
        data = new Event[]{};
    }

    public Event[] getEvents() {
        return data;
    }

    public void setEvents(Event[] data) {
        this.data = data;
    }
}
