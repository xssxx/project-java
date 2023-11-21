package cs211.project.controllers.event;

import cs211.project.models.event.Event;
import cs211.project.models.event.Schedule;
import cs211.project.models.event.ScheduleList;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.ScheduleListFileDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EventCardController {
    private Event event; // All event data
    private Datasource<ScheduleList> datasource;
    private ScheduleList scheduleList;

    // FXML components
    @FXML private Label eventName;
    @FXML private Rectangle eventImage;
    @FXML private Label joinCountLabel;
    @FXML private Label statusLabel;

    // Constructor
    public EventCardController() {
        datasource = new ScheduleListFileDatasource("data/event", "schedule-list.csv");
        scheduleList = datasource.readData();
    }

    // Set data for the event card
    public void setData(Event event) {
        this.event = event;
        Event currentEvent = event;
        joinCountLabel.setText(event.getJoinCount() + "/" + event.getMaxJoinCount());
        eventName.setText(event.getEventName());

        Image image = new Image(event.getImgSrc());
        eventImage.setFill(new ImagePattern(image));

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

    @FXML
    public void onClick() throws IOException {
        FXRouter.goTo("event-detail", event);
    }
}
