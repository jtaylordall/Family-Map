package com.example.familymap.model;

public class EventWrapper {
    private Event[] data;

    public EventWrapper(){
        data = new Event[]{};
    }

    public Event[] getEvents() {
        return data;
    }

    public void setEvents(Event[] data) {
        this.data = data;
    }
}
