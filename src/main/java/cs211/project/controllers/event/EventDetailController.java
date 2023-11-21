package cs211.project.controllers.event;

import animatefx.animation.FadeIn;
import cs211.project.controllers.dynamicPane.DynamicPaneController;
import cs211.project.models.event.Event;
import cs211.project.models.event.EventList;
import cs211.project.models.event.Schedule;
import cs211.project.models.event.ScheduleList;
import cs211.project.models.history.History;
import cs211.project.models.history.HistoryList;
import cs211.project.models.teamEvent.Participant;
import cs211.project.models.teamEvent.ParticipantList;
import cs211.project.models.teamEvent.Team;
import cs211.project.models.teamEvent.TeamList;
import cs211.project.models.user.AccountManager;
import cs211.project.models.user.User;
import cs211.project.services.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventDetailController extends DynamicPaneController {
    @FXML private Rectangle eventImage;
    @FXML private Label eventName;
    @FXML private Label start;
    @FXML private Label end;
    @FXML private Label description;
    @FXML private Label creator;
    @FXML private Label joinCount;
    @FXML private ChoiceBox<Team> teamChoice;
    @FXML private TableView<Schedule> scheduleTable;
    @FXML private Label joinErrorLabel;
    @FXML private Button joinEventButton;
    @FXML private Label startTime;
    @FXML private Label endTime;
    @FXML private Button eventMemberListButton;
    @FXML private Button teamMemberListButton;

    private Datasource<TeamList> teamListDatasource;
    private Datasource<ScheduleList> scheduleListDatasource;
    private Datasource<ParticipantList> participantListDatasource;
    private Datasource<HistoryList> historyListDatasource;
    private Datasource<EventList> eventListDatasource;
    private EventList eventList;
    private TeamList teamList;
    private ScheduleList scheduleList;
    private ParticipantList participantList;
    private HistoryList historyList;
    private Event currentEvent;
    private Team currentTeam;
    private User currentUser;
    private Participant currentParticipant;

    @FXML
    private void initialize() throws IOException {
        joinErrorLabel.setText("");
        root.setLeft(FXMLLoader.load(getClass().getResource("/cs211/project/views/sidebar.fxml")));

        teamListDatasource = new TeamListFileDatasource("data", "team-list.csv");
        scheduleListDatasource = new ScheduleListFileDatasource("data/event", "schedule-list.csv");
        participantListDatasource = new ParticipantListFileDatasource("data", "participant-list.csv");
        historyListDatasource = new HistoryListFileDatasource("data", "history-list.csv");
        eventListDatasource = new EventListFileDatasource("data/event", "event-list.csv");

        teamList = teamListDatasource.readData();
        scheduleList = scheduleListDatasource.readData();
        participantList = participantListDatasource.readData();
        historyList = historyListDatasource.readData();
        eventList = eventListDatasource.readData();

        currentEvent = (Event) FXRouter.getData();
        currentUser = AccountManager.getInstance().getCurrentUser();
        currentTeam = teamChoice.getValue();
        currentParticipant = participantList.findParticipantByEvent(currentUser.getUserName(), currentEvent.getEventName());

        joinEventButton.setVisible(false);

        if (currentParticipant == null) {
            joinEventButton.setVisible(true);
            teamMemberListButton.setVisible(false);
            eventMemberListButton.setVisible(false);
        }

        if (currentParticipant != null) {
            eventMemberListButton.setVisible(currentParticipant.getRole().equals("Create"));
        }

        if (currentEvent.getJoinCount() == currentEvent.getMaxJoinCount()) {
            joinEventButton.setVisible(false);
        }

        // if event is ended hide join button
        LocalDateTime endedTime = LocalDateTime.of(currentEvent.getEnd(), currentEvent.getEndTime());
        if (LocalDateTime.now().compareTo(endedTime) > 0) {
            joinEventButton.setVisible(false);
        }

        // listening to team choice if value change display new schedule based on team
        teamChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentTeam = newValue;
                showTable(newValue);
            }
        });

        showTeamChoice(teamList);;
        showData(currentEvent);

        new FadeIn(root).play();
    }

    private void showTeamChoice(TeamList teamList) {
        String currentEventName = currentEvent.getEventName();
        for (Team team : teamList.getTeamList()) {
            // get only team that in selected event
            if (!team.isInEvent(currentEventName))
                continue;
            // set default value
            if (teamChoice.getValue() == null)
                teamChoice.setValue(team);

            teamChoice.getItems().add(team);
        }
    }

    private void showData(Event event) {
        eventName.setText(event.getEventName());
        Image image = new Image(event.getImgSrc());
        eventImage.setFill(new ImagePattern(image));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        start.setText(event.getStart().format(formatter).toString());
        end.setText(event.getEnd().format(formatter).toString());
        description.setText(event.getDescription());
        creator.setText(event.getCreatorName());
        joinCount.setText(Integer.toString(event.getJoinCount()));
        startTime.setText(event.getStartTime().toString());
        endTime.setText(event.getEndTime().toString());
    }

    private void showTable(Team team) {
        TableColumn<Schedule, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setPrefWidth(100);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? null : date.format(dateFormatter));
            }
        });

        TableColumn<Schedule, String> startTimeColumn = new TableColumn<>("Start Time");
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        startTimeColumn.setPrefWidth(80);

        TableColumn<Schedule, String> endTimeColumn = new TableColumn<>("End Time");
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        endTimeColumn.setPrefWidth(80);

        TableColumn<Schedule, String> activityColumn = new TableColumn<>("Activity");
        activityColumn.setCellValueFactory(new PropertyValueFactory<>("activity"));
        activityColumn.setPrefWidth(200);

        TableColumn<Schedule, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setPrefWidth(300);

        TableColumn<Schedule, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setPrefWidth(70);

        scheduleTable.getColumns().clear();
        scheduleTable.getColumns().add(dateColumn);
        scheduleTable.getColumns().add(startTimeColumn);
        scheduleTable.getColumns().add(endTimeColumn);
        scheduleTable.getColumns().add(activityColumn);
        scheduleTable.getColumns().add(statusColumn);
        scheduleTable.getColumns().add(descriptionColumn);
        scheduleTable.getItems().clear();

        String currentTeamName = team.getTeamName();
        String currentEventName = currentEvent.getEventName();
        // continue if schedule is not in current event and not in current team
        for (Schedule schedule : scheduleList.getScheduleList()) {
            if (!schedule.isInTeam(currentTeamName) || !schedule.isInEvent(currentEventName))
                continue;

            scheduleTable.getItems().add(schedule);
        }
    }

    @FXML
    private void onClickJoin() {
        currentUser = AccountManager.getInstance().getCurrentUser();

        // write new participant
        Participant newParticipant = new Participant(currentUser.getUserName(), "Join", currentEvent.getEventName(), currentTeam.getTeamName());
        participantList.addParticipant(newParticipant);
        participantListDatasource.writeData(participantList);

        // write new history
        History newHistory = new History(currentUser.getUserName(), currentEvent.getImgSrc(), currentEvent.getEventName(), LocalDate.now(), "Join");
        historyList.addHistory(newHistory);
        historyListDatasource.writeData(historyList);

        // add join count to event
        String name = currentEvent.getEventName();
        Event currentEvent = eventList.findByEventName(name);
        currentEvent.addJoinCount();
        eventListDatasource.writeData(eventList);

        try {
            FXRouter.goTo("event");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void viewTeamMemberList() throws IOException {
        FXRouter.goTo("team-event-edit", currentTeam);
    }

    @FXML
    private void viewEventMemberList() throws IOException {
        FXRouter.goTo("event-member-list", currentTeam);
    }
}
