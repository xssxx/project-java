package cs211.project.controllers.teamEvent;

import cs211.project.controllers.dynamicPane.DynamicPaneController;
import cs211.project.models.event.Event;
import cs211.project.models.event.ScheduleList;
import cs211.project.models.teamEvent.CommentList;
import cs211.project.models.teamEvent.ParticipantList;
import cs211.project.models.teamEvent.Team;
import cs211.project.models.teamEvent.TeamList;
import cs211.project.models.user.AccountManager;
import cs211.project.models.user.User;
import cs211.project.services.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class TeamEventCreateController extends DynamicPaneController {
    @FXML TextField nameTextField;
    @FXML ListView<Team> teamListView;
    @FXML private Label errorLabel;

    private Datasource<TeamList> teamListDatasource;
    private Datasource<ParticipantList> participantListDatasource;
    private Datasource<CommentList> commentListDatasource;
    private Datasource<ScheduleList> scheduleListDatasource;
    private ParticipantList participantList;
    private CommentList commentList;
    private ScheduleList scheduleList;
    private TeamList teamList;
    private Event currentEvent;
    private Team selectedTeam;

    @FXML
    private void initialize() throws IOException {
        root.setLeft(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/cs211/project/views/sidebar.fxml"))));
        teamListDatasource = new TeamListFileDatasource("data", "team-list.csv");
        participantListDatasource = new ParticipantListFileDatasource("data", "participant-list.csv");
        commentListDatasource = new CommentListFileDatasource("data", "comment-list.csv");
        scheduleListDatasource = new ScheduleListFileDatasource("data/event", "schedule-list.csv");

        teamList = teamListDatasource.readData();
        participantList = participantListDatasource.readData();
        scheduleList = scheduleListDatasource.readData();
        commentList = commentListDatasource.readData();

        currentEvent = (Event) FXRouter.getData();
        errorLabel.setText("");
        teamListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedTeam = newValue;
            }
        });

        showTable();
    }

    private void showTable() {
        for (Team team : teamList.getTeamList()) {
            if (team.isInEvent(currentEvent.getEventName())) {
                teamListView.getItems().add(team);
            }
        }
    }

    @FXML
    private void onAddTeam() {
        String eventName = currentEvent.getEventName();
        String teamName = nameTextField.getText();

        errorLabel.setText("");

        if (eventName.isEmpty() || teamName.isEmpty()) {
            errorLabel.setText("Please input the team name.");
            return;
        }

        for (Team team : teamList.getTeamList()) {
            if (team.isInEvent(eventName) && team.getTeamName().equals(teamName)) {
                errorLabel.setText("This team already exists.");
                return;
            }
        }

        User creator = AccountManager.getInstance().getCurrentUser();
        // add creator to participant datasource
        participantList.addParticipant(creator.getUserName(), "Create", eventName, teamName);
        participantListDatasource.writeData(participantList);

        // add team to team-list datasource
        Team newTeam = new Team(teamName, eventName);
        teamList.addTeam(newTeam);
        teamListView.getItems().add(newTeam);
        teamListDatasource.writeData(teamList);
        teamListView.refresh();
    }

    @FXML
    private void onDeleteTeam() throws IOException {
        if (selectedTeam == null) {
            errorLabel.setText("Please select a team.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Delete Team");
        confirmationAlert.setContentText("Are you sure you want to delete the selected team?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.orElse(ButtonType.CANCEL) == ButtonType.OK) {
            String teamNameToRemove = selectedTeam.getTeamName();
            teamList.removeTeam(selectedTeam);
            commentList.removeByTeamName(teamNameToRemove);
            scheduleList.removeByTeamName(teamNameToRemove);
            participantList.getParticipants().removeIf(participant -> participant.getEventName().equals(currentEvent.getEventName()) &&
                    participant.getTeamName().equals(teamNameToRemove));

            teamListDatasource.writeData(teamList);
            participantListDatasource.writeData(participantList);
            commentListDatasource.writeData(commentList);
            scheduleListDatasource.writeData(scheduleList);

            FXRouter.goTo("team-event-create");
        }
    }

    @FXML
    private void onEditActivity() throws IOException {
        if (selectedTeam == null) {
            errorLabel.setText("Please select team.");
            return;
        }
        FXRouter.goTo("team-event-management", selectedTeam);
    }

    @FXML
    private void onClickBack() throws IOException {
        FXRouter.goTo("edit-event-list");
    }

}
