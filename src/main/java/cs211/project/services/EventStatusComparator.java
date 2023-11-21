package cs211.project.services;

import cs211.project.models.event.Event;

import java.time.LocalDateTime;
import java.util.Comparator;

public class EventStatusComparator implements Comparator<Event> {
    @Override
    public int compare(Event o1, Event o2) {
        LocalDateTime now = LocalDateTime.now();

        // Combine date and time for comparison
        LocalDateTime o1EndDateTime = LocalDateTime.of(o1.getEnd(), o1.getEndTime());
        LocalDateTime o2EndDateTime = LocalDateTime.of(o2.getEnd(), o2.getEndTime());

        // Compare based on the status (Upcoming < Ongoing < Ended)
        if (o1.getStart().isAfter(now.toLocalDate()) && o2.getStart().isAfter(now.toLocalDate())) {
            LocalDateTime o1StartDateTime = LocalDateTime.of(o1.getStart(), o1.getStartTime());
            LocalDateTime o2StartDateTime = LocalDateTime.of(o2.getStart(), o2.getStartTime());
            return o2StartDateTime.compareTo(o1StartDateTime);
        } else if (o1EndDateTime.isAfter(now) && o2EndDateTime.isAfter(now)) {
            return o2EndDateTime.compareTo(o1EndDateTime);
        } else {
            if (o1EndDateTime.isAfter(now)) {
                return -1;
            } else if (o2EndDateTime.isAfter(now)) {
                return 1;
            } else {
                return o2EndDateTime.compareTo(o1EndDateTime);
            }
        }
    }
}

