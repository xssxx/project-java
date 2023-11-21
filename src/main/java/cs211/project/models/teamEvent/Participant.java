package cs211.project.models.teamEvent;

import java.util.Objects;

public class Participant {
    private String username;
    private String role;
    private String eventName;
    private String teamName;

    public Participant(String username, String role, String eventName, String teamName) {
        this.username = username;
        this.role = role;
        this.eventName = eventName;
        this.teamName = teamName;
    }

    public String getUsername() {
        return username;
    }
    public String getRole() {
        return role;
    }

    public String getEventName() {
        return eventName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(role, that.role) &&
                Objects.equals(eventName, that.eventName) &&
                Objects.equals(teamName, that.teamName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, role, eventName, teamName);
    }
}