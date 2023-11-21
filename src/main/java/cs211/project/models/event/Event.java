package cs211.project.models.event;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private String eventName;
    private LocalDate start;
    private LocalDate end;
    private String imgSrc;
    private int joinCount;
    private int maxJoinCount;
    private String creatorName;
    private String creatorImgSrc;
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;

    public Event(String eventName, String imageSource) {
        this.eventName = eventName;
        this.imgSrc = imageSource;
    }

    public Event(String eventName, String imgSrc, LocalDate start, LocalDate end, int joinCount, String creatorName, String creatorImgSrc, String description, int maxJoinCount, LocalTime startTime, LocalTime endTime) {
        this.eventName = eventName;
        this.start = start;
        this.end = end;
        this.imgSrc = imgSrc;
        this.joinCount = joinCount;
        this.creatorName = creatorName;
        this.creatorImgSrc = creatorImgSrc;
        this.description = description;
        this.maxJoinCount = maxJoinCount;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void addJoinCount() {
        joinCount++;
    }

    public void decreaseJoinCount() {
        joinCount--;
    }

    public int getMaxJoinCount() {
        return maxJoinCount;
    }

    public String getEventName() {
        return eventName;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public int getJoinCount() {
        return joinCount;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getCreatorImgSrc() { return creatorImgSrc; }

    public String getDescription() { return description; }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatorImgSrc(String creatorImgSrc) {
        this.creatorImgSrc = creatorImgSrc;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", imgSrc='" + imgSrc + '\'' +
                ", joinCount=" + joinCount +
                ", maxJoinCount=" + maxJoinCount +
                ", creatorName='" + creatorName + '\'' +
                ", creatorImgSrc='" + creatorImgSrc + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
