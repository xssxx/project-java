package cs211.project.controllers.event;

import animatefx.animation.FadeIn;
import animatefx.animation.SlideInUp;
import com.dlsc.gemsfx.SearchTextField;
import cs211.project.controllers.dynamicPane.DynamicPaneController;
import cs211.project.models.event.Event;
import cs211.project.models.event.EventList;
import cs211.project.models.user.AccountManager;
import cs211.project.models.user.User;
import cs211.project.services.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EventPageController extends DynamicPaneController {
    @FXML private GridPane grid;
    @FXML private ScrollPane scrollpane;
    @FXML private SearchTextField searchField;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private Circle profileImage;
    @FXML private Label profileLabel;

    private EventList events;
    private EventListFileDatasource datasource;
    private Map<String, Event> eventIndex;

    @FXML
    private void initialize() throws IOException {
        datasource = new EventListFileDatasource("data/event", "event-list.csv");
        events = datasource.readData();
        root.setLeft(FXMLLoader.load(getClass().getResource("/cs211/project/views/sidebar.fxml")));

        User userCurrent = AccountManager.getInstance().getCurrentUser();
        Image image = new Image(userCurrent.getImgSrc());
        profileImage.setFill(new ImagePattern(image));
        profileLabel.setText(userCurrent.getUserName());

        eventIndex = buildEventIndex(events);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                EventList filteredEvents = filterEvents(newValue);
                showGrid(filteredEvents);
            } else {
                showGrid(events);
            }
        });

        String[] typeOfSort = {"Name", "Least Join", "Status"};
        filterComboBox.getItems().addAll(typeOfSort);
        filterComboBox.setOnAction(this::handledSort);

        showGrid(events);
        // defer focus to root to deselect search field
        Platform.runLater(() -> {
            root.requestFocus();
        });

        FadeIn fadeIn = new FadeIn(root);
        fadeIn.setSpeed(2.0);
        fadeIn.play();
    }

    private Map<String, Event> buildEventIndex(EventList eventList) {
        Map<String, Event> index = new HashMap<>();

        for (Event event : eventList.getEvents()) {
            index.put(event.getEventName().toLowerCase(), event);
        }

        return index;
    }

    private EventList filterEvents(String query) {
        EventList filteredEvents = new EventList();

        for (String eventName : eventIndex.keySet()) {
            if (eventName.contains(query.toLowerCase())) {
                filteredEvents.addEvent(eventIndex.get(eventName));
            }
        }

        return filteredEvents;
    }

    private void showGrid(EventList events) {
        grid.getChildren().clear();
        scrollpane.setPannable(true);

        int columns = 0;
        int rows = 1;

        for (Event event : events.getEvents()) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/cs211/project/views/events/event-card.fxml"));
            VBox eventCard;
            try {
                eventCard = fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            HBox statusJoin = (HBox) eventCard.lookup("#statusJoin");
            if (event.getJoinCount() < event.getMaxJoinCount() / 2) {
                statusJoin.getStyleClass().clear();
                statusJoin.getStyleClass().add("count-card-green");
            } else if (event.getJoinCount() >= event.getMaxJoinCount() / 2 && event.getJoinCount() < event.getMaxJoinCount()) {
                statusJoin.getStyleClass().clear();
                statusJoin.getStyleClass().add("count-card-orange");
            } else if (event.getJoinCount() == event.getMaxJoinCount()){
                statusJoin.getStyleClass().clear();
                statusJoin.getStyleClass().add("count-card-red");
            }

            EventCardController eventCardController = fxmlLoader.getController();
            eventCardController.setData(event);

            if (columns == 3) {
                columns = 0;
                ++rows;
            }

            grid.add(eventCard, columns++, rows);
            GridPane.setMargin(eventCard, new Insets(10));
        }
        SlideInUp slideInUp = new SlideInUp(grid);
        slideInUp.setSpeed(2.0);
        slideInUp.play();
    }

    private void handledSort(ActionEvent event){
        if (filterComboBox.getValue().equals("Name")){
            events.sort(new EventNameComparator());
        } else if (filterComboBox.getValue().equals("Least Join")) {
            events.sort(new EventJoinCountComparator());
        } else if (filterComboBox.getValue().equals("Status")) {
            events.sort(new EventStatusComparator());
        }
        showGrid(events);
    }

}