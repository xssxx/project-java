package cs211.project.services;

import cs211.project.models.history.History;

import java.util.Comparator;
import java.util.List;

public class HistoryNameComparator implements Comparator<History> {
    @Override
    public int compare(History o1, History o2){
        return (o1.getEventName().compareTo(o2.getEventName()));
    }
}
