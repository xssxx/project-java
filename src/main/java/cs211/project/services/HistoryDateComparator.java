package cs211.project.services;

import cs211.project.models.history.History;

import java.util.Comparator;

public class HistoryDateComparator implements Comparator<History> {
    @Override
    public int compare(History o1, History o2){
        return (o1.getEventDate().compareTo(o2.getEventDate()));
    }
}
