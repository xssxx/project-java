package cs211.project.controllers.teamEvent;

import animatefx.animation.SlideInUp;
import cs211.project.controllers.dynamicPane.DynamicPaneController;
import cs211.project.models.teamEvent.Participant;
import cs211.project.models.teamEvent.ParticipantList;
import cs211.project.models.teamEvent.Team;
import cs211.project.models.user.AccountManager;
import cs211.project.models.user.User;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.ParticipantListFileDatasource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

public class TeamEventEditController extends DynamicPaneController {
    @FXML private GridPane grid;
    @FXML private ScrollPane scrollPane;
    @FXML private Button addMemberButton;
    private ParticipantList participants;
    private Datasource<ParticipantList> datasource;
    private Team currentTeam;

    @FXML
    private void initialize() throws IOException {
        datasource = new ParticipantListFileDatasource("data", "participant-list.csv");
        currentTeam = (Team) FXRouter.getData();
        root.setLeft(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/cs211/project/views/sidebar.fxml"))));
        addMemberButton.setVisible(false);
        participants = datasource.readData();

        User currentUser = AccountManager.getInstance().getCurrentUser();
        Participant participant = participants.findParticipantByTeam(currentUser.getUserName(), currentTeam.getTeamName());

        if (participant != null && "Create".equals(participant.getRole())) {
            addMemberButton.setVisible(true);
        }

        showGrid();
    }

    private void showGrid() throws IOException {
        participants = datasource.readData();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPannable(true);

        int columns = 3;
        int rows = 1;

        for (Participant participant : participants.getParticipants()) {
            if (participant.getEventName().equals(currentTeam.getEventName()) && participant.getTeamName().equals(currentTeam.getTeamName())) {
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
            }
        }
        new SlideInUp(grid).play();
    }

    @FXML
    private void onAddMember() {
        try {
            FXRouter.goTo("team-event-add", currentTeam);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onClickBack() throws IOException {
        FXRouter.goTo("event-detail");
    }

}
