package cs211.project.controllers.teamEvent;

import cs211.project.models.event.Event;
import cs211.project.models.event.EventList;
import cs211.project.models.teamEvent.Participant;
import cs211.project.models.teamEvent.ParticipantList;
import cs211.project.models.teamEvent.Team;
import cs211.project.models.user.User;
import cs211.project.models.user.UserList;
import cs211.project.services.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class MemberCardAddController {
    @FXML private Circle userImage;
    @FXML private Label userNameLabel;
    @FXML private Label nameLabel;
    private Participant participant;
    private Team currentTeam;
    private UserList userList;
    private ParticipantList participantList;
    private EventList eventList;
    private Datasource<UserList> userListDatasource;
    private Datasource<ParticipantList> participantListDatasource;
    private Datasource<EventList> eventListDatasource;


    @FXML
    private void initialize() {
        eventListDatasource = new EventListFileDatasource("data/event/", "event-list.csv");
        userListDatasource = new UserListFileDatasource("data", "user-list.csv");
        participantListDatasource = new ParticipantListFileDatasource("data", "participant-list.csv");
        userList = userListDatasource.readData();
        participantList = participantListDatasource.readData();
        eventList = eventListDatasource.readData();
    }

    public void setData(Participant participant, Team team) {
        User user = userList.findUserByUsername(participant.getUsername());

        nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        userNameLabel.setText(user.getUserName());

        Image image = new Image(user.getImgSrc());
        userImage.setFill(new ImagePattern(image));
        this.participant = participant;
        currentTeam = team;

    }

    @FXML
    private void onAddMember() throws IOException {
        Participant newParticipant = new Participant(participant.getUsername(), participant.getRole(), currentTeam.getEventName(), currentTeam.getTeamName());
        participantList.addParticipant(newParticipant);
        participantListDatasource.writeData(participantList);

        Event currentEvent = eventList.findByEventName(currentTeam.getEventName());
        currentEvent.addJoinCount();
        eventListDatasource.writeData(eventList);

        FXRouter.goTo("team-event-add");
    }
}
