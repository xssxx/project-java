package cs211.project.controllers.teamEvent;


import cs211.project.models.report.ReportList;
import cs211.project.models.event.Event;
import cs211.project.models.event.EventList;
import cs211.project.models.teamEvent.Participant;
import cs211.project.models.teamEvent.ParticipantList;
import cs211.project.models.teamEvent.Team;
import cs211.project.models.teamEvent.TeamList;
import cs211.project.models.user.AccountManager;
import cs211.project.models.user.User;
import cs211.project.models.user.UserList;
import cs211.project.services.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import java.io.IOException;
import java.util.Objects;


public class MemberCardKickController {
    @FXML private Circle userImage;
    @FXML private Label userNameLabel;
    @FXML private Label nameLabel;
    @FXML private Label roleLabel;
    @FXML private Button kickButton;
    @FXML private Button reportButton;
    private Datasource<UserList> userListDatasource;
    private Datasource<ParticipantList> participantListDatasource;
    private Datasource<ReportList> reportListDatasource;
    private Datasource<EventList> eventListDatasource;
    private UserList userList;
    private ParticipantList participantList;
    private EventList eventList;
    private Participant participant;
    private ReportList reportList;
    private Team currentTeam;

    @FXML
    private void initialize() {
        userListDatasource = new UserListFileDatasource("data", "user-list.csv");
        participantListDatasource = new ParticipantListFileDatasource("data", "participant-list.csv");
        reportListDatasource = new ReportListFileDatasource("data","report-list.csv");
        eventListDatasource = new EventListFileDatasource("data/event/", "event-list.csv");

        currentTeam = (Team) FXRouter.getData();

        userList = userListDatasource.readData();
        participantList = participantListDatasource.readData();
        reportList = reportListDatasource.readData();
        eventList = eventListDatasource.readData();
    }

    public void setData(Participant participant) {
        User currentUser = AccountManager.getInstance().getCurrentUser();
        User user = userList.findUserByUsername(participant.getUsername());
        String currentUserRole = participantList.findParticipantByTeam(currentUser.getUserName(), currentTeam.getTeamName()).getRole();

        kickButton.setVisible(true);
        reportButton.setVisible(true);

        if (participant.getRole().equals("Create")) {
            roleLabel.setText("Owner");
            kickButton.setVisible(false);
        } else if (participant.getRole().equals("Join")) {
            roleLabel.setText("Participant");

            if (!currentUserRole.equals("Create")) {
                kickButton.setVisible(false);
            }

        }

        if (currentUser.getUserName().equals(participant.getUsername())) {
            kickButton.setVisible(false);
            reportButton.setVisible(false);
        }

        nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        userNameLabel.setText(user.getUserName());

        Image image = new Image(user.getImgSrc());
        userImage.setFill(new ImagePattern(image));
        this.participant = participant;

    }

    @FXML
    private void onKickMember() throws IOException {
        participantList.removeParticipant(participant);
        participantListDatasource.writeData(participantList);

        Event currentEvent = eventList.findByEventName(currentTeam.getEventName());
        currentEvent.decreaseJoinCount();
        eventListDatasource.writeData(eventList);

        FXRouter.goTo("event-detail");
    }
    @FXML
    private void onReportMember() {
        User currentUser = AccountManager.getInstance().getCurrentUser();
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Report Member");
        alert.setHeaderText("Enter a reason for reporting the member:");
        TextField textField = new TextField();
        VBox content = new VBox();
        content.getChildren().add(textField);
        alert.getDialogPane().setContent(content);
        ButtonType AlertType = null;
        alert.getButtonTypes().addAll(AlertType.OK, AlertType.CANCEL);

        alert.showAndWait().ifPresent(response -> {
            if (response == AlertType.OK) {
                String reason = textField.getText();
                if (reason.isEmpty()) {
                    return;
                }
                reportList.addReport(currentUser.getUserName(),participant.getUsername(),reason);
                reportListDatasource.writeData(reportList);
                User user = userList.findUserByUsername(participant.getUsername());
                user.addReportCount();
                userListDatasource.writeData(userList);
            }
        });
    }


}

