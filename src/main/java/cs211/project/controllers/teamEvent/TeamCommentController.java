package cs211.project.controllers.teamEvent;

import animatefx.animation.FadeIn;
import cs211.project.controllers.dynamicPane.DynamicPaneController;
import cs211.project.models.teamEvent.*;
import cs211.project.models.event.Event;
import cs211.project.models.event.EventList;
import cs211.project.models.teamEvent.Comment;
import cs211.project.models.teamEvent.CommentList;
import cs211.project.models.teamEvent.Team;
import cs211.project.models.teamEvent.TeamList;
import cs211.project.models.user.AccountManager;
import cs211.project.models.user.User;
import cs211.project.services.CommentListFileDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.ParticipantListFileDatasource;
import cs211.project.services.EventListFileDatasource;
import cs211.project.services.TeamListFileDatasource;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.Objects;

public class TeamCommentController extends DynamicPaneController {
    @FXML private TextField commentTextField;
    @FXML private VBox chatBox;
    @FXML private VBox commentBox;
    @FXML private ScrollPane commentScrollPane;
    @FXML private Circle iconChat;
    @FXML private Label teamLabel;
    @FXML private Line lineChat;
    @FXML private Label commentLabel;
    @FXML private Button commentEnter;

    private Rectangle selectedCardTeam = null;
    private Datasource<EventList> eventListDatasource;
    private Datasource<CommentList> commentListDatasource;
    private Datasource<TeamList> teamListDatasource;
    private Datasource<ParticipantList> participantListDatasource;
    private EventList eventList;
    private CommentList commentList;
    private TeamList teamList;
    private ParticipantList participantList;
    private Team selectedTeam;

    @FXML
    private void initialize() throws IOException {
        teamLabel.setText("");
        root.setLeft(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/cs211/project/views/sidebar.fxml"))));

        commentListDatasource = new CommentListFileDatasource("data", "comment-list.csv");
        teamListDatasource = new TeamListFileDatasource("data", "team-list.csv");
        participantListDatasource = new ParticipantListFileDatasource("data", "participant-list.csv");
        eventListDatasource = new EventListFileDatasource("data/event","event-list.csv");

        commentList = commentListDatasource.readData();
        teamList = teamListDatasource.readData();
        participantList = participantListDatasource.readData();
        eventList = eventListDatasource.readData();

        showTeam();
        new FadeIn(root).play();
    }

    private void showTeam() throws IOException {

        for (Team team : teamList.getTeamList()) {
            User currentUser = AccountManager.getInstance().getCurrentUser();
            if ((participantList.findParticipantByTeam(currentUser.getUserName(), team.getTeamName()) != null)) {
                // get team card
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/cs211/project/views/teams/team-card.fxml"));
                AnchorPane card = fxmlLoader.load();
                TeamCardController teamCardController = fxmlLoader.getController();
                teamCardController.setData(team);
                // set selected team when click on card
                card.setOnMouseClicked(event -> {
                    if(selectedCardTeam != null){
                        selectedCardTeam.getStyleClass().remove("select-team");
                    }
                    Rectangle bgTeam = (Rectangle) card.lookup("#bgTeam");
                    selectedCardTeam = bgTeam;

                    selectedTeam = team;
                    showComment(team);

                    bgTeam.getStyleClass().clear();
                    bgTeam.getStyleClass().add("select-team");
                });

                // add to chat box
                chatBox.getChildren().add(card);

            }
        }
    }


    private void showComment(Team team) {
        commentBox.getChildren().clear();
        teamLabel.setText(team.getEventName() + " " + team.getTeamName());
        lineChat.getStyleClass().clear();
        lineChat.getStyleClass().add("show-line-comment");
        commentLabel.setDisable(false);
        commentTextField.setDisable(false);
        commentEnter.setDisable(false);
        commentLabel.setDisable(false);
        for(Event event : eventList.getEvents()){
            if(event.getEventName().equals(team.getEventName())){
                Image image = new Image(event.getImgSrc());
                iconChat.setFill(new ImagePattern(image));
            }
        }
        for (Comment comment : commentList.getCommentList()) {
            if (comment.getTeamName().equals(team.getTeamName())) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/cs211/project/views/teams/comment-card.fxml"));

                HBox card = null;
                try {
                    card = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                CommentCardController commentCardController = fxmlLoader.getController();
                commentCardController.setData(comment);

                commentBox.getChildren().add(card);
            }
        }

        Platform.runLater(() -> {
            commentScrollPane.setVvalue(1);
        });
    }

    @FXML
    private void enter() {
        if (!commentTextField.getText().isEmpty()) {
            User currentUser = AccountManager.getInstance().getCurrentUser();
            Comment comment = new Comment(selectedTeam.getEventName(), selectedTeam.getTeamName(), currentUser.getUserName(), commentTextField.getText());
            commentList.addComment(comment);
            commentTextField.clear();
            commentListDatasource.writeData(commentList);
            showComment(selectedTeam);
        }
    }
}