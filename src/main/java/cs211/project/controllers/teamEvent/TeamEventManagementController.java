package cs211.project.controllers.teamEvent;

import com.dlsc.gemsfx.TimePicker;
import cs211.project.controllers.dynamicPane.DynamicPaneController;
import cs211.project.models.event.Event;
import cs211.project.models.event.EventList;
import cs211.project.models.event.Schedule;
import cs211.project.models.event.ScheduleList;
import cs211.project.models.teamEvent.ParticipantList;
import cs211.project.models.teamEvent.Team;
import cs211.project.models.teamEvent.TeamList;
import cs211.project.services.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class TeamEventManagementController extends DynamicPaneController {
    @FXML private Label errorLabel;
    @FXML private TableView<Schedule> teamActivityTable;
    @FXML private TextField nameTextField;
    @FXML private TextArea infomationTextArea;
    @FXML private TimePicker startTimePicker;
    @FXML private TimePicker endTimePicker;
    @FXML private DatePicker datePicker;

    private Datasource<ParticipantList> participantListDatasource;
    private Datasource<ScheduleList> scheduleListDatasource;
    private Datasource<TeamList> teamListDatasource;
    private Datasource<EventList> eventListDatasource;
    private ScheduleList scheduleList;
    private ParticipantList participants;
    private Team currentTeam;
    private TeamList teamList;
    private EventList eventList;
    private Schedule currentSchedule;


    @FXML
    private void initialize() throws IOException {
        root.setLeft(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/cs211/project/views/sidebar.fxml"))));

        participantListDatasource = new ParticipantListFileDatasource("data", "participant-list.csv");
        scheduleListDatasource = new ScheduleListFileDatasource("data/event", "schedule-list.csv");
        teamListDatasource = new TeamListFileDatasource("data", "team-list.csv");
        eventListDatasource = new EventListFileDatasource("data/event", "event-list.csv");

        participants = participantListDatasource.readData();
        scheduleList = scheduleListDatasource.readData();
        teamList = teamListDatasource.readData();
        eventList = eventListDatasource.readData();

        currentTeam = (Team) FXRouter.getData();;
        errorLabel.setText("");

        teamActivityTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentSchedule = newValue;
            }
        });

        showTable(currentTeam);
    }

    private void showTable(Team team) {
        TableColumn<Schedule, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setPrefWidth(100);

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

        teamActivityTable.getColumns().clear();
        teamActivityTable.getColumns().add(dateColumn);
        teamActivityTable.getColumns().add(startTimeColumn);
        teamActivityTable.getColumns().add(endTimeColumn);
        teamActivityTable.getColumns().add(activityColumn);
        teamActivityTable.getColumns().add(descriptionColumn);
        teamActivityTable.getColumns().add(statusColumn);
        teamActivityTable.getItems().clear();

        String currentTeamName = team.getTeamName();
        String currentEventName = team.getEventName();

        for (Schedule schedule : scheduleList.getScheduleList()) {
            if (!schedule.isInTeam(currentTeamName) || !schedule.isInEvent(currentEventName)) {
                continue;
            }
            teamActivityTable.getItems().add(schedule);
        }
    }

    @FXML
    private void onAdd() {
        Event event = eventList.findByEventName(currentTeam.getEventName());
        String eventName = currentTeam.getEventName();
        String activity = nameTextField.getText();
        LocalDate date = datePicker.getValue();
        LocalTime startTime = startTimePicker.getTime();
        LocalTime endTime = endTimePicker.getTime();
        String description = infomationTextArea.getText().replace("\n", " ");
        String teamName = currentTeam.getTeamName();

        try {
            if (validateTeamData(event, activity, date, startTime, endTime, description, teamName)) {
                Schedule newSchedule = new Schedule(eventName, date, startTime, endTime, activity, teamName, description, "UNDONE");
                scheduleList.addSchedule(newSchedule);
                teamActivityTable.getItems().add(newSchedule);
                scheduleListDatasource.writeData(scheduleList);
                teamActivityTable.refresh();
            }
        } catch (Exception e) {
            errorLabel.setText("An error occurred.");
        }
    }

    private boolean validateTeamData(Event event, String activity, LocalDate date, LocalTime startTime, LocalTime endTime, String description, String teamName) {
        errorLabel.setText("");

        for (Schedule schedule : scheduleList.getScheduleList()) {
            if (schedule.isInTeam(currentTeam.getTeamName()) && schedule.getActivity().equals(activity)) {
                errorLabel.setText("This activity already exists.");
                return false;
            }
        }

        if (activity.isEmpty() || description.isEmpty() || date == null) {
            errorLabel.setText("Activity, information, and date cannot be empty.");
            return false;
        } else if (date.isBefore(event.getStart())) {
            errorLabel.setText("The date cannot be before the event's start date.");
            return false;
        } else if (date.isAfter(event.getEnd())) {
            errorLabel.setText("The date cannot be after the event's end date.");
            return false;
        } else if (startTime.isAfter(endTime)) {
            errorLabel.setText("The start time cannot be after the end time.");
            return false;
        }
        return true;
    }

    @FXML
    private void onDelete() throws IOException {
        scheduleList.removeSchedule(currentSchedule);
        scheduleListDatasource.writeData(scheduleList);
        FXRouter.goTo("team-event-management");
    }

    @FXML
    private void onStatus() {
        if (currentSchedule.getStatus().equals("UNDONE")) {
            currentSchedule.setStatus("DONE");
        } else if (currentSchedule.getStatus().equals("DONE")){
            currentSchedule.setStatus("UNDONE");
        }
        scheduleListDatasource.writeData(scheduleList);
        teamActivityTable.refresh();
    }

    @FXML
    private void onBack() {
        try {
            FXRouter.goTo("team-event-create");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
