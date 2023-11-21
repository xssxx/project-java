package cs211.project.models.teamEvent;

import java.util.ArrayList;
import java.util.Iterator;

public class ParticipantList {

    private ArrayList<Participant> participants;

    public ParticipantList() {
        participants = new ArrayList<Participant>();
    }

    public void addParticipant(String username, String role, String eventName, String teamName) {
        Participant participant = new Participant(username, role, eventName, teamName);
        participants.add(participant);
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }
    public void removeParticipant(Participant participant) {
        participants.remove(participant);
    }
    public void removeByEventName(String eventName) {
        Iterator<Participant> iterator = participants.iterator();

        while (iterator.hasNext()) {
            Participant participant = iterator.next();
            if (participant.getEventName().equals(eventName)) {
                iterator.remove();
            }
        }
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public Participant findParticipantByEvent(String username, String eventName) {
        for (Participant participant : participants) {
            if (participant.getUsername().equals(username) && participant.getEventName().equals(eventName)) {
                return participant;
            }
        }
        return null;
    }

    public Participant findParticipantByTeam(String username, String teamName) {
        for (Participant participant : participants) {
            if (participant.getUsername().equals(username) && participant.getTeamName().equals(teamName)) {
                return participant;
            }
        }
        return null;
    }

}

