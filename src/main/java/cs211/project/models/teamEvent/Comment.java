package cs211.project.models.teamEvent;

public class Comment {
    private String eventName;
    private String teamName;
    private String username;
    private String text;

    public Comment(String eventName, String teamName, String username, String text) {
        this.eventName = eventName;
        this.teamName = teamName;
        this.username = username;
        this.text = text;
    }

    public String getEventName() {
        return eventName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return username + ": " + text ;
    }
}