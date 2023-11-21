package cs211.project.models.event;

import cs211.project.services.ImageReader;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EventList {
    private ArrayList<Event> events;

    public EventList() {
        events = new ArrayList<>();
    }

    public void addEvent(String eventName, String imgSrc, LocalDate start, LocalDate end, int joinedCount, String creatorName, String creatorImgSrc, String description, int maxJoinCount, LocalTime startTime, LocalTime endTime) {
        if (!eventName.isEmpty() && !imgSrc.isEmpty() && start != null && end != null && joinedCount >= 0 && !creatorName.isEmpty() && !creatorImgSrc.isEmpty() && !description.isEmpty() && startTime != null && endTime != null) {
            String eventImg = ImageReader.getImagePath("data/images/events/", imgSrc);
            String creatorImg = ImageReader.getImagePath("data/images/profiles/", creatorImgSrc);

            events.add(new Event(eventName, eventImg, start, end, joinedCount, creatorName, creatorImg, description, maxJoinCount, startTime, endTime));
        }
    }

    public boolean removeEvent(Event event) {
        return events.remove(event);
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public Event findByEventName(String eventName) {
        for (Event event : events) {
            if (event.getEventName().equals(eventName)) {
                return event;
            }
        }
        return null;
    }

    public void sort(Comparator<Event> cmp){
        Collections.sort(events, cmp);
    }
}