package cs211.project.models.teamEvent;

import java.util.ArrayList;
import java.util.Iterator;

public class TeamList {
    private ArrayList<Team> teamList;

    public TeamList() {
        teamList = new ArrayList<>();
    }

    public void addTeam(String teamName, String eventName) {
        teamList.add(new Team(teamName, eventName));
    }

    public void addTeam(Team team) {
        teamList.add(team);
    }

    public void removeTeam(Team team) {
        teamList.remove(team);
    }
    public void removeByEventName(String eventName) {
        Iterator<Team> iterator = teamList.iterator();

        while (iterator.hasNext()) {
            Team team = iterator.next();
            if (team.getEventName().equals(eventName)) {
                iterator.remove();
            }
        }
    }

    public ArrayList<Team> getTeamList() {
        return teamList;
    }
}
