package cs211.project.models.history;

import java.time.LocalDate;

public class History implements Comparable<History>{
    private String User;
    private String eventImage;
    private String eventName;
    private LocalDate eventDate;
    private String eventAction;

    @Override
    public int compareTo(History o){
        return (eventName.compareTo(o.eventName));
    }

    public History(String User,String eventImage, String eventName, LocalDate eventDate, String eventAction) {
        this.User = User;
        this.eventImage = eventImage;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventAction = eventAction;
    }

    public String getUser() {
        return  User;
    }

    public String getEventImage() {
        return eventImage;
    }

    public String getEventName() {
        return eventName;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public String getEventAction() {
        return eventAction;
    }

    public boolean isUsername(String username){
        return User.equals(username);
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    @Override
    public String toString() {
        return  eventName + " " + eventDate + " " + eventAction;
    }

}
