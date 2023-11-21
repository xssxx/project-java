package cs211.project.controllers.admin;

import animatefx.animation.SlideInUp;
import cs211.project.controllers.dynamicPane.DynamicPaneController;
import cs211.project.models.user.User;
import cs211.project.models.user.UserList;
import cs211.project.services.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AdminController extends DynamicPaneController {
    @FXML private GridPane grid;
    @FXML private ScrollPane scrollpane;
    @FXML private ChoiceBox<String> choiceBox;

    private UserList users;
    private UserListFileDatasource datasource;
    private String[] sort={"Username", "Date", "Report Count", "Latest Login"};

    @FXML
    private void initialize() throws IOException {
        datasource = new UserListFileDatasource("data", "user-list.csv");
        root.setLeft(FXMLLoader.load(getClass().getResource("/cs211/project/views/sidebar.fxml")));
        users = datasource.readData();
        showGrid(users);
        choiceBox.getItems().addAll(sort);
        choiceBox.setOnAction(this::handleSort);
        choiceBox.setValue("Report Count");
    }

    private void handleSort(ActionEvent event) {
        users = datasource.readData();
        if (choiceBox.getValue().equals("Date")){
            users.sort(new UserDateComparator());
        } else if (choiceBox.getValue().equals("Report Count")){
            users.sort(new UserReportCountComparator());
        } else if (choiceBox.getValue().equals("Username")){
            users.sort(new UserNameComparator());
        } else if (choiceBox.getValue().equals("Latest Login")) {
            users.sort(new UserLoginComparator());
        }
        try {
            showGrid(users);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showGrid(UserList users) throws IOException {
        grid.getChildren().clear();
        scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollpane.setPannable(true);
        int columns = 0;
        int rows = 1;
        for (User user: users.getUsers()) {
            if(user.getRole().equals("admin")){
                continue;
            }
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/cs211/project/views/user-card.fxml"));
            VBox userCard = fxmlLoader.load();

            UserCardController userCardController = fxmlLoader.getController();
            userCardController.setData(user);

            if (columns == 1) {
                columns = 0;
                ++rows;
            }

            grid.add(userCard, columns++, rows);
            GridPane.setMargin(userCard, new Insets(5));
        }
        new SlideInUp(grid).play();
    }

}
