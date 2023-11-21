package cs211.project.models.history;

import cs211.project.services.ImageReader;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class HistoryList {
    private ArrayList<History> historyArrayList;

    public HistoryList() {historyArrayList = new ArrayList<>(); }

    public void addHistory(String User, String eventImage, String eventName, LocalDate eventDate, String eventAction) {
        if (!User.isEmpty()) {
            String img = ImageReader.getImagePath("data/images/events", eventImage);
            historyArrayList.add(new History(User, img, eventName, eventDate, eventAction));
        }
    }

    public void removeHistoryByEventName(String eventName) {
        Iterator<History> iterator = historyArrayList.iterator();

        while (iterator.hasNext()) {
            History history = iterator.next();
            if (history.getEventName().equals(eventName)) {
                iterator.remove();
            }
        }
    }


    public History findByEventName(String eventName) {
        for (History history : historyArrayList) {
            if (history.getEventName().equals(eventName)) {
                return history;
            }
        }
        return null;
    }

    public void addHistory(History history) {
        historyArrayList.add(history);
    }

    public void sort(Comparator<History> cmp){
        Collections.sort(historyArrayList, cmp);
    }

    public ArrayList<History> getHistoryArrayList() {
        return historyArrayList;
    }

}
