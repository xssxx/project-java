package cs211.project.controllers.history;

import animatefx.animation.FadeIn;
import cs211.project.controllers.dynamicPane.DynamicPaneController;
import cs211.project.models.history.History;
import cs211.project.models.history.HistoryList;
import cs211.project.models.user.AccountManager;
import cs211.project.models.user.User;
import cs211.project.services.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class HistoryEventController extends DynamicPaneController {
    @FXML private BorderPane root;
    @FXML private VBox historyVBox;
    @FXML private ScrollPane scrollPane;
    @FXML private ComboBox<String> filterComboBox;
    private HistoryList historyList;
    private Datasource<HistoryList> datasource;

    @FXML
    private void initialize() throws IOException {
        root.setLeft(FXMLLoader.load(getClass().getResource("/cs211/project/views/sidebar.fxml")));
        datasource = new HistoryListFileDatasource("data", "history-list.csv");
        scrollPane.setPannable(true);
        historyList = datasource.readData();
        showHistory(historyList);
        String[] typeOfSort = {"Name", "Date", "Action", "Status"};
        filterComboBox.getItems().addAll(typeOfSort);
        filterComboBox.setOnAction(this::handledSort);
        new FadeIn(root).play();
    }

    private void showHistory(HistoryList historyList) throws IOException {
        historyVBox.getChildren().clear();
        User currentUser = AccountManager.getInstance().getCurrentUser();
        for (History history : historyList.getHistoryArrayList()) {
            if(history.isUsername(currentUser.getUserName())){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/cs211/project/views/history-card.fxml"));
                HBox historyCard = fxmlLoader.load();
                HistoryCardController historyCardController = fxmlLoader.getController();
                historyCardController.setData(history);
                historyVBox.getChildren().add(historyCard);
            }
        }
    }

    private void handledSort(ActionEvent event){
        if (filterComboBox.getValue().equals("Name")) {
            historyList.sort(new HistoryNameComparator());
        } else if (filterComboBox.getValue().equals("Date")) {
            historyList.sort(new HistoryDateComparator());
        } else if (filterComboBox.getValue().equals("Action")) {
            historyList.sort(new HisoryEventTypeComparator());
        } else if (filterComboBox.getValue().equals("Status")) {
            historyList.sort(new HistoryStatusComparator());
        }

        try {
            showHistory(historyList);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}
