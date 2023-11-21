package cs211.project.models.teamEvent;

public class Team {
    private String teamName;
    private String eventName;

    public Team(String teamName, String eventName) {
        this.teamName = teamName;
        this.eventName = eventName;
    }

    public boolean isInEvent(String eventName) {
        return this.eventName.equals(eventName);
    }

    public String getTeamName() {
        return teamName;
    }

    public String getEventName() {
        return eventName;
    }

    @Override
    public String toString() {
        return eventName + " : " + teamName;
    }
}

