package cs211.project.controllers.teamEvent;

import animatefx.animation.SlideInUp;
import cs211.project.controllers.dynamicPane.DynamicPaneController;
import cs211.project.models.teamEvent.Participant;
import cs211.project.models.teamEvent.ParticipantList;
import cs211.project.models.teamEvent.Team;
import cs211.project.services.Datasource;
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

public class AddMemberController extends DynamicPaneController {
    @FXML private GridPane grid;
    @FXML private ScrollPane scrollPane;

    private ParticipantList participants;
    private Datasource<ParticipantList> datasource;
    private Team currentTeam;

    @FXML
    private void initialize() throws IOException {
        datasource = new ParticipantListFileDatasource("data", "participant-list.csv");
        currentTeam = (Team) FXRouter.getData();
        root.setLeft(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/cs211/project/views/sidebar.fxml"))));
        showGrid();
    }

    private void showGrid() throws IOException {
        participants = datasource.readData();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPannable(true);

        int columns = 3;
        int rows = 1;

        ArrayList<String> unJoinedParticipant = new ArrayList<>();
        for (Participant participant : participants.getParticipants()) {
            if (participant.getTeamName().equals(currentTeam.getTeamName()) &&
                    participant.getEventName().equals(currentTeam.getEventName())) {
                unJoinedParticipant.add(participant.getUsername());
            }
        }

        grid.getChildren().clear();

        for (Participant participant : participants.getParticipants()) {
            if (!unJoinedParticipant.contains(participant.getUsername()) &&
                    participant.getEventName().equals(currentTeam.getEventName())) {

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/cs211/project/views/teams/member-card-add.fxml"));
                VBox memberCard = fxmlLoader.load();

                MemberCardAddController memberCardAddController = fxmlLoader.getController();
                memberCardAddController.setData(participant, currentTeam);

                if (columns == 3) {
                    columns = 0;
                    rows++;
                }

                grid.add(memberCard, columns++, rows);
                GridPane.setMargin(memberCard, new Insets(10));
            }

        }
        new SlideInUp(grid).play();
    }


    @FXML
    private void onClickBack() {
        try {
            FXRouter.goTo("team-event-edit");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
