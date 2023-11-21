package cs211.project.controllers.event;


import animatefx.animation.FadeIn;
import cs211.project.controllers.dynamicPane.DynamicPaneController;
import cs211.project.models.event.Event;
import cs211.project.models.event.EventList;
import cs211.project.models.event.ScheduleList;
import cs211.project.models.history.HistoryList;
import cs211.project.models.teamEvent.CommentList;
import cs211.project.models.teamEvent.ParticipantList;
import cs211.project.models.teamEvent.TeamList;
import cs211.project.models.user.AccountManager;
import cs211.project.models.user.User;
import cs211.project.services.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class EditEventListController extends DynamicPaneController {
    @FXML private TableView<Event> eventTable;
    @FXML private Label errorLabel;
    @FXML private Rectangle imagePreview;
    private Datasource<TeamList> teamListDatasource;
    private Datasource<CommentList> commentListDatasource;
    private Datasource<ParticipantList> participantListDatasource;
    private Datasource<EventList> eventListDatasource;
    private Datasource<HistoryList> historyListDatasource;
    private Datasource<ScheduleList> scheduleListDatasource;
    private HistoryList historyList;
    private EventList eventList;
    private TeamList teamList;
    private ParticipantList participantList;
    private CommentList commentList;
    private ScheduleList scheduleList;
    private Event selectedEvent;


    @FXML
    private void initialize() throws IOException {
        root.setLeft(FXMLLoader.load(getClass().getResource("/cs211/project/views/sidebar.fxml")));
        errorLabel.setText("");

        imagePreview.setVisible(false);

        eventListDatasource = new EventListFileDatasource("data/event", "event-list.csv");
        historyListDatasource = new HistoryListFileDatasource("data", "history-list.csv");
        teamListDatasource = new TeamListFileDatasource("data", "team-list.csv");
        commentListDatasource = new CommentListFileDatasource("data", "comment-list.csv");
        participantListDatasource = new ParticipantListFileDatasource("data", "participant-list.csv");
        scheduleListDatasource = new ScheduleListFileDatasource("data/event", "schedule-list.csv");

        eventList = eventListDatasource.readData();
        historyList = historyListDatasource.readData();
        commentList = commentListDatasource.readData();
        teamList = teamListDatasource.readData();
        participantList = participantListDatasource.readData();
        scheduleList = scheduleListDatasource.readData();

        showEventTable(eventList);

        eventTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedEvent = newValue;
                showImagePreview(newValue);
            }
        });

        new FadeIn(root).play();
    }

    private void showImagePreview(Event event) {
        imagePreview.setVisible(true);
        Image image = new Image(event.getImgSrc());
        imagePreview.setFill(new ImagePattern(image));
    }

    private void showEventTable(EventList eventList) {
        TableColumn<Event, String> nameColumn = new TableColumn<>("Event Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        nameColumn.setPrefWidth(300);

        TableColumn<Event, String> startColumn = new TableColumn<>("Start Date");
        startColumn.setPrefWidth(76);
        startColumn.setCellValueFactory(cellData -> {
            LocalDate startDate = cellData.getValue().getStart();
            return new SimpleStringProperty(startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        });

        TableColumn<Event, String> endColumn = new TableColumn<>("End Date");
        endColumn.setPrefWidth(76);
        endColumn.setCellValueFactory(cellData -> {
            LocalDate endDate = cellData.getValue().getEnd();
            return new SimpleStringProperty(endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        });

        TableColumn<Event, Integer> joinColumn = new TableColumn<>("Join Count");
        joinColumn.setPrefWidth(76);
        joinColumn.setCellValueFactory(new PropertyValueFactory<>("joinCount"));


        eventTable.getColumns().clear();
        eventTable.getColumns().addAll(nameColumn, startColumn, endColumn, joinColumn);

        eventTable.getItems().clear();

        User currentUser = AccountManager.getInstance().getCurrentUser();
        for (Event event : eventList.getEvents()) {

            if (event.getCreatorName().equals(currentUser.getUserName())) {
                eventTable.getItems().add(event);
            }
        }
    }

    @FXML
    private void onClickEditEvent() throws IOException {
        if (selectedEvent == null) {
            errorLabel.setText("Please select event");
        } else {
            FXRouter.goTo("edit-event", selectedEvent);
        }
    }

    @FXML
    private void onDeleteEvent() {
        if (selectedEvent == null) {
            errorLabel.setText("Please select an event");
            return; // Exit early if no event is selected
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Are you sure you want to delete the event?");
        alert.setContentText(selectedEvent.getEventName());

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String eventNameToRemove = selectedEvent.getEventName();

            // Remove the event from the list
            if (eventList.removeEvent(selectedEvent) ) {
                // If the event was removed, update the data and refresh the UI
                historyList.removeHistoryByEventName(eventNameToRemove);
                teamList.removeByEventName(eventNameToRemove);
                commentList.removeByEventName(eventNameToRemove);
                participantList.removeByEventName(eventNameToRemove);
                scheduleList.removeByEventName(eventNameToRemove);

                eventListDatasource.writeData(eventList);
                historyListDatasource.writeData(historyList);
                teamListDatasource.writeData(teamList);
                commentListDatasource.writeData(commentList);
                participantListDatasource.writeData(participantList);
                scheduleListDatasource.writeData(scheduleList);

                eventTable.refresh();
                showEventTable(eventList);
                imagePreview.setFill(null);
            }
        }
    }


    @FXML
    private void onClickEditTeam() throws IOException {
        if (selectedEvent == null) {
            errorLabel.setText("Please select event");
        } else {
            FXRouter.goTo("team-event-create", selectedEvent);
        }
    }

}
