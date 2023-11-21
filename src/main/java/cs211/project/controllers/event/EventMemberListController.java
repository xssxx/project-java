package cs211.project.controllers.event;

import animatefx.animation.SlideInUp;
import cs211.project.controllers.dynamicPane.DynamicPaneController;
import cs211.project.controllers.teamEvent.MemberCardKickController;
import cs211.project.models.event.Event;
import cs211.project.models.event.EventList;
import cs211.project.models.teamEvent.Participant;
import cs211.project.models.teamEvent.ParticipantList;
import cs211.project.models.teamEvent.Team;
import cs211.project.services.Datasource;
import cs211.project.services.EventListFileDatasource;
import cs211.project.services.FXRouter;
import cs211.project.services.ParticipantListFileDatasource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class EventMemberListController extends DynamicPaneController {

    @FXML private GridPane grid;
    @FXML private ScrollPane scrollPane;
    private ParticipantList participants;
    private Datasource<ParticipantList> participantListDatasource;
    private Datasource<EventList> eventListDatasource;
    private Team currentTeam;
    private Event currentEvent;
    private EventList eventList;

    @FXML
    private void initialize() throws IOException {
        currentTeam = (Team) FXRouter.getData();

        participantListDatasource = new ParticipantListFileDatasource("data", "participant-list.csv");
        eventListDatasource = new EventListFileDatasource("data/event", "event-list.csv");

        participants = participantListDatasource.readData();
        eventList = eventListDatasource.readData();

        currentEvent = eventList.findByEventName(currentTeam.getEventName());

        root.setLeft(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/cs211/project/views/sidebar.fxml"))));
        showGrid();
    }

    private void showGrid() throws IOException {
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPannable(true);

        int columns = 3;
        int rows = 1;

        ArrayList<String> addedParticipants = new ArrayList<>();

        for (Participant participant : participants.getParticipants()) {
            if (participant.getEventName().equals(currentEvent.getEventName())
                    && !addedParticipants.contains(participant.getUsername())) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/cs211/project/views/teams/member-card-kick.fxml"));
                VBox memberCard = fxmlLoader.load();

                MemberCardKickController memberCardKickController = fxmlLoader.getController();
                memberCardKickController.setData(participant);

                if (columns == 3) {
                    columns = 0;
                    rows++;
                }

                grid.add(memberCard, columns++, rows);
                GridPane.setMargin(memberCard, new Insets(10));

                addedParticipants.add(participant.getUsername());
            }
        }
        new SlideInUp(grid).play();
    }

    @FXML
    private void onClickBack() throws IOException {
        FXRouter.goTo("event-detail");
    }
}
