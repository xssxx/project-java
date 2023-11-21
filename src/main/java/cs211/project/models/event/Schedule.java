package cs211.project.models.event;

import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
    private String eventName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String activity;
    private String teamName;
    private String description;
    private String status;

    public Schedule(String eventName, LocalDate date, LocalTime startTime, LocalTime endTime, String activity, String teamName, String description, String status) {

        this.eventName = eventName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.activity = activity;
        this.teamName = teamName;
        this.description = description;
        this.status = status;
    }

    public boolean isInTeam(String teamName) {
        return this.teamName.equals(teamName);
    }

    public boolean isInEvent(String eventName) {
        return this.eventName.equals(eventName);
    }

    public LocalDate getDate() { return date; }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }

    public String getEventName() {
        return eventName;
    }

    public String getActivity() {
        return activity;
    }

    public String getDescription() {
        return description;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
