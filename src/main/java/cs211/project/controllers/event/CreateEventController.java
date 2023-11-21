package cs211.project.controllers.event;

import animatefx.animation.FadeIn;
import com.dlsc.gemsfx.TimePicker;
import cs211.project.controllers.dynamicPane.DynamicPaneController;
import cs211.project.models.event.Event;
import cs211.project.models.event.EventList;
import cs211.project.models.history.HistoryList;
import cs211.project.models.teamEvent.ParticipantList;
import cs211.project.models.user.AccountManager;
import cs211.project.models.user.User;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class CreateEventController extends DynamicPaneController {
    @FXML private DatePicker start;
    @FXML private DatePicker end;
    @FXML private TextArea description;
    @FXML private TextField eventNameField;
    @FXML private Rectangle image;
    @FXML private Label saveErrorLabel;
    @FXML private TextField maxJoinCountField;
    @FXML private TimePicker startTimePicker;
    @FXML private TimePicker endTimePicker;
    private String imgSrc;
    private Datasource<EventList> eventListDatasource;
    private Datasource<HistoryList> historyListDatasource;
    private Datasource<ParticipantList> participantListDatasource;
    private EventList eventList;
    private HistoryList historyList;
    private ParticipantList participantList;

    @FXML
    private void initialize() throws IOException {
        root.setLeft(FXMLLoader.load(getClass().getResource("/cs211/project/views/sidebar.fxml")));
        clearError();

        image.setFill(new ImagePattern(new Image(getClass().getResource("/cs211/project/images/assets/browse-banner.png").toString())));

        eventListDatasource = new EventListFileDatasource("data/event", "event-list.csv");
        historyListDatasource = new HistoryListFileDatasource("data","history-list.csv");
        participantListDatasource = new ParticipantListFileDatasource("data", "participant-list.csv");

        eventList = eventListDatasource.readData();
        historyList = historyListDatasource.readData();
        participantList = participantListDatasource.readData();

        new FadeIn(root).play();
    }

    @FXML
    private void onSave() throws IOException {
        String eventNameText = eventNameField.getText();
        String descriptionText = description.getText().replace("\n", " ");
        LocalDate startDate = start.getValue();
        LocalDate endDate = end.getValue();
        LocalTime startTime = startTimePicker.getTime();
        LocalTime endTime = endTimePicker.getTime();

        if (validateEventData(eventNameText, descriptionText, startDate, endDate, maxJoinCountField.getText(), startTime, endTime)) {
            User currentUser = AccountManager.getInstance().getCurrentUser();
            String[] creatorImgName = currentUser.getImgSrc().split("\\\\");
            int maxJoinCount = Integer.parseInt(maxJoinCountField.getText());
            LocalDate currentDate = LocalDate.now();


            historyList.addHistory(currentUser.getUserName(),imgSrc,eventNameText,currentDate,"Create");
            // 1 join count is creator
            eventList.addEvent(eventNameText, imgSrc, startDate, endDate, 1, currentUser.getUserName(), creatorImgName[creatorImgName.length - 1], descriptionText, maxJoinCount, startTime, endTime);
            // add event to datasource
            eventListDatasource.writeData(eventList);
            // add create history to datasource
            historyListDatasource.writeData(historyList);

            clearFields();
            FXRouter.goTo("event");
        }
    }

    private boolean validateEventData(String eventNameText, String descriptionText, LocalDate startDate, LocalDate endDate, String maxJoinCountText, LocalTime startTime, LocalTime endTime) {
        saveErrorLabel.setText("");

        if (startTime == null || endTime == null) {
            saveErrorLabel.setText("Please fill in all required fields.");
            return false;
        }

        if (eventNameText.isEmpty() || descriptionText.isEmpty() || imgSrc == null || startDate == null || endDate == null) {
            saveErrorLabel.setText("Please fill in all required fields.");
            return false;
        }

        if (startDate.isAfter(endDate)) {
            saveErrorLabel.setText("The start date cannot be after the end date.");
            return false;
        }

        if (startDate.equals(endDate) && startTime.isAfter(endTime)) {
            saveErrorLabel.setText("The start time cannot be after the end time.");
            return false;
        }

        for (Event event : eventList.getEvents()) {
            if (event.getEventName().equals(eventNameText)) {
                saveErrorLabel.setText("This event already exists.");
                return false;
            }
        }

        try {
            Integer.parseInt(maxJoinCountText);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    @FXML
    private void addImage(MouseEvent event) {
        saveErrorLabel.setText("");

        if (event.getButton() == MouseButton.PRIMARY) { // Check if left mouse button is clicked
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
                    String filename = LocalDate.now() + "_" + System.currentTimeMillis() + "."
                            + fileSplit[fileSplit.length - 1];
                    Path target = FileSystems.getDefault().getPath(
                            destDir.getAbsolutePath() + System.getProperty("file.separator") + filename
                    );

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

    private void clearFields() {
        eventNameField.clear();
        description.clear();
        saveErrorLabel.setText("");
    }

    private void clearError() {
        saveErrorLabel.setText("");
    }

}