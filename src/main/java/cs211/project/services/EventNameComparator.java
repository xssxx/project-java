package cs211.project.services;

import cs211.project.models.event.Event;

import java.util.Comparator;

public class EventNameComparator implements Comparator<Event> {
    @Override
    public int compare(Event o1, Event o2) {
        return (o1.getEventName().compareTo(o2.getEventName()));
    }
}
