package cs211.project.controllers.event;

import animatefx.animation.FadeIn;
import com.dlsc.gemsfx.TimePicker;
import cs211.project.controllers.dynamicPane.DynamicPaneController;
import cs211.project.models.event.Event;
import cs211.project.models.event.EventList;
import cs211.project.models.history.History;
import cs211.project.models.history.HistoryList;
import cs211.project.services.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditEventController extends DynamicPaneController {
    @FXML private DatePicker start;
    @FXML private DatePicker end;
    @FXML private TextArea description;
    @FXML private TextField eventName;
    @FXML private Rectangle image;
    @FXML private Label saveErrorLabel;
    @FXML private TimePicker startTimePicker;
    @FXML private TimePicker endTimePicker;

    private Datasource<EventList> eventListDatasource;
    private Datasource<HistoryList> historyListDatasource;
    private HistoryList historyList;
    private EventList eventList;
    private Event event; // current event
    private String imgSrc;

    @FXML
    private void initialize() throws IOException {
        root.setLeft(FXMLLoader.load(getClass().getResource("/cs211/project/views/sidebar.fxml")));
        event = (Event) FXRouter.getData(); // selected event

        eventListDatasource = new EventListFileDatasource("data/event", "event-list.csv");
        historyListDatasource = new HistoryListFileDatasource("data", "history-list.csv");
        historyList = historyListDatasource.readData();
        eventList = eventListDatasource.readData();

        start.setConverter(new StringConverter<LocalDate>() {
            private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        end.setConverter(new StringConverter<LocalDate>() {
            private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        clearErrorLabel();
        showData(event);

        new FadeIn(root).play();
    }

    private void showData(Event event) {
        eventName.setText(event.getEventName());
        description.setText(event.getDescription());
        Image img = new Image(event.getImgSrc());
        imgSrc = event.getImgSrc();
        image.setFill(new ImagePattern(img));
        start.setValue(event.getStart());
        end.setValue(event.getEnd());
        startTimePicker.setTime(event.getStartTime());
        endTimePicker.setTime(event.getEndTime());
    }

    @FXML
    private void onSave() throws IOException {
        Event currentEvent = eventList.findByEventName(event.getEventName());
        History currentHistory = historyList.findByEventName(currentEvent.getEventName());
        if (currentEvent == null) return;
        // edit event
        currentEvent.setEventName(eventName.getText());
        currentEvent.setStart(start.getValue());
        currentEvent.setEnd(end.getValue());
        currentEvent.setDescription(description.getText().replace("\n", " "));
        currentEvent.setImgSrc(imgSrc);
        currentEvent.setStartTime(startTimePicker.getTime());
        currentEvent.setEndTime(endTimePicker.getTime());
        // edit history
        currentHistory.setEventDate(LocalDate.now()); // latest action date
        currentHistory.setEventImage(currentEvent.getImgSrc());
        currentHistory.setEventName(currentEvent.getEventName());

        // write event to event-list.csv
        eventListDatasource.writeData(eventList);
        // write history
        historyListDatasource.writeData(historyList);


        FXRouter.goTo("event");
    }

    @FXML
    private void addImage(MouseEvent event) {
        if (eventName.getText().isEmpty()) {
            saveErrorLabel.setText("Please fill in the event name first.");
            return;
        }

        saveErrorLabel.setText("");

        if (event.getButton() == MouseButton.PRIMARY) {
            FileChooser chooser = new FileChooser();
            chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            Node source = (Node) event.getSource();
            File file = chooser.showOpenDialog(source.getScene().getWindow());

            if (file != null) {
                try {
                    File destDir = new File("data/images/events");
                    if (!destDir.exists()) {
                        destDir.mkdirs();
                    }

                    String[] fileSplit = file.getName().split("\\.");
                    String extension = fileSplit[fileSplit.length - 1];
                    String filename = eventName.getText() + "." + extension;
                    Path target = Paths.get(destDir.getAbsolutePath(), filename);

                    Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);

                    Image img = new Image(target.toUri().toString());
                    Rectangle rectangle = new Rectangle(320, 200);
                    rectangle.setFill(new ImagePattern(img));

                    image.setFill(new ImagePattern(img));
                    imgSrc = filename;
                } catch (IOException e) {
                    e.printStackTrace();
                    saveErrorLabel.setText("Error while handling the image.");
                }
            }
        }
    }

    @FXML
    private void onBack() throws IOException {
        FXRouter.goTo("edit-event-list");
    }

    private void clearErrorLabel() {
        saveErrorLabel.setText("");
    }

}
