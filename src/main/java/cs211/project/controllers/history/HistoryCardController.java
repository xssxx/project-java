package cs211.project.controllers.history;

import cs211.project.models.event.Event;
import cs211.project.models.event.EventList;
import cs211.project.models.event.ScheduleList;
import cs211.project.models.history.History;
import cs211.project.models.teamEvent.TeamList;
import cs211.project.services.Datasource;
import cs211.project.services.EventListFileDatasource;
import cs211.project.services.ScheduleListFileDatasource;
import cs211.project.services.TeamListFileDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HistoryCardController {
    @FXML private Rectangle ImageRectangle;
    @FXML private Label eventLabel;
    @FXML private Label dateLabel;
    @FXML private Label actionLabel;
    @FXML private Label statusLabel;
    private Datasource<EventList> eventListDatasource;
    private Datasource<ScheduleList> scheduleListDatasource;
    private Datasource<TeamList> teamListDatasource;
    private EventList eventList;
    private ScheduleList scheduleList;
    private TeamList teamList;

    @FXML
    private void initialize() {
        eventListDatasource = new EventListFileDatasource("data/event", "event-list.csv");
        scheduleListDatasource = new ScheduleListFileDatasource("data/event", "schedule-list.csv");
        teamListDatasource = new TeamListFileDatasource("data", "team-list.csv");
        eventList = eventListDatasource.readData();
        scheduleList = scheduleListDatasource.readData();
        teamList = teamListDatasource.readData();
    }

    public void setData(History history) {
        Image image = new Image(history.getEventImage());
        ImageRectangle.setFill(new ImagePattern(image));
        eventLabel.setText(history.getEventName());
        String formattedDate = history.getEventDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        dateLabel.setText(formattedDate);
        actionLabel.setText(history.getEventAction());

        Event currentEvent = eventList.findByEventName(history.getEventName());

        // Check if the event is upcoming, ongoing, or ended
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        LocalDate eventStartDate = currentEvent.getStart();
        LocalTime eventStartTime = currentEvent.getStartTime();
        LocalDate eventEndDate = currentEvent.getEnd();
        LocalTime eventEndTime = currentEvent.getEndTime();

        if (currentDate.isBefore(eventStartDate) || (currentDate.isEqual(eventStartDate) && currentTime.isBefore(eventStartTime))) {
            // Upcoming event
            statusLabel.setText("Upcoming");
        } else if ((currentDate.isEqual(eventStartDate) && currentTime.isAfter(eventStartTime)) ||
                (currentDate.isAfter(eventStartDate) && currentDate.isBefore(eventEndDate)) ||
                (currentDate.isEqual(eventEndDate) && currentTime.isBefore(eventEndTime))) {
            // Ongoing event
            statusLabel.setText("Ongoing");
        } else {
            // Ended event
            statusLabel.setText("Ended");
        }
    }

}
