package cs211.project.services;

import cs211.project.models.event.Event;
import cs211.project.models.event.EventList;
import cs211.project.models.history.History;

import java.time.LocalDateTime;
import java.util.Comparator;

public class HistoryStatusComparator implements Comparator<History> {
    private Datasource<EventList> datasource;
    private EventList eventList;

    public HistoryStatusComparator() {
        datasource = new EventListFileDatasource("data/event", "event-list.csv");
        eventList = datasource.readData();
    }

    @Override
    public int compare(History o1, History o2) {
        Event e1 = eventList.findByEventName(o1.getEventName());
        Event e2 = eventList.findByEventName(o2.getEventName());

        LocalDateTime e1EndDateTime = LocalDateTime.of(e1.getEnd(), e1.getEndTime());
        LocalDateTime e2EndDateTime = LocalDateTime.of(e2.getEnd(), e2.getEndTime());

        return e2EndDateTime.compareTo(e1EndDateTime);
    }
}
