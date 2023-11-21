package cs211.project.controllers.admin;

import cs211.project.controllers.dynamicPane.DynamicPaneController;
import cs211.project.models.history.History;
import cs211.project.models.history.HistoryList;
import cs211.project.models.report.Report;
import cs211.project.models.report.ReportList;
import cs211.project.models.user.UserList;
import cs211.project.services.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import cs211.project.models.user.User;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import java.io.IOException;

public class AdminUserController extends DynamicPaneController {
    @FXML private Rectangle imageUser;
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label reportcountrLabel;
    @FXML private Label statusLabel;
    @FXML private Button banButton;
    @FXML private ListView<History> historyListView;
    @FXML private ListView<Report> reportListView;

    private Datasource<UserList> userListDatasource;
    private Datasource<HistoryList> historyListDatasource;
    private Datasource<ReportList> reportListDatasource;

    private UserList userList;
    private HistoryList historyList;
    private ReportList reportList;

    @FXML
    private void initialize() throws IOException {
        root.setLeft(FXMLLoader.load(getClass().getResource("/cs211/project/views/sidebar.fxml")));
        User user = (User) FXRouter.getData();
        userListDatasource = new UserListFileDatasource("data", "user-list.csv");
        userList = userListDatasource.readData();


        historyListDatasource = new HistoryListFileDatasource("data","history-list.csv");
        historyList = historyListDatasource.readData();

        reportListDatasource =new ReportListFileDatasource("data","report-list.csv");
        reportList =reportListDatasource.readData();

        if (user.getStatus().equals("Available")) {
            banButton.setText("Ban");
        } else {
            banButton.setText("Unban");
        }
        showData(user);
    }

    private void showData(User user) {
        Image image = new Image(user.getImgSrc());
        imageUser.setFill(new ImagePattern(image));
        firstNameLabel.setText(user.getFirstName());
        lastNameLabel.setText(user.getLastName());
        usernameLabel.setText(user.getUserName());
        reportcountrLabel.setText(Integer.toString(user.getReportCount()));
        statusLabel.setText(user.getStatus());
        for (History userHistory : historyList.getHistoryArrayList()) {
            if (userHistory.isUsername(user.getUserName())) {
                historyListView.getItems().add(userHistory);
            }
        }
        for (Report userReport : reportList.getReportList()) {
            if (userReport.isUserReported(user.getUserName())) {
                reportListView.getItems().add(userReport);
            }
        }
    }
    @FXML
    private void onBack() throws IOException {
        FXRouter.goTo("admin");
    }

    @FXML
    private void onBan() {
        User user = (User) FXRouter.getData();

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType confirmButton;
        ButtonType cancelButton;
        if (user.getStatus().equals("Available")) {
            confirmationAlert.setTitle("Confirm Ban");
            confirmationAlert.setHeaderText("Ban User");
            confirmationAlert.setContentText("Are you sure you want to ban the user " + user.getUserName() + "?");

            confirmButton = new ButtonType("Ban");
            cancelButton = new ButtonType("Cancel");
            confirmationAlert.getButtonTypes().setAll(confirmButton, cancelButton);

            confirmationAlert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == confirmButton) {
                    Alert banAlert = new Alert(Alert.AlertType.INFORMATION);
                    banAlert.setTitle("User Banned");
                    banAlert.setHeaderText(null);
                    banAlert.setContentText("The user " + user.getUserName() + " has been banned.");
                    banAlert.showAndWait();
                    User current = userList.findUserByUsername(user.getUserName());
                    current.setStatus("Banned");
                    userListDatasource.writeData(userList);
                    statusLabel.setText("Banned");
                    try {
                        FXRouter.goTo("admin");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } else {
            confirmationAlert.setTitle("Confirm Unban");
            confirmationAlert.setHeaderText("Unban User");
            confirmationAlert.setContentText("Are you sure you want to unban the user " + user.getUserName() + "?");

            confirmButton = new ButtonType("Unban");
            cancelButton = new ButtonType("Cancel");
            confirmationAlert.getButtonTypes().setAll(confirmButton, cancelButton);

            confirmationAlert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == confirmButton) {
                    Alert banAlert = new Alert(Alert.AlertType.INFORMATION);
                    banAlert.setTitle("User Unbanned");
                    banAlert.setHeaderText(null);
                    banAlert.setContentText("The user " + user.getUserName() + " has been unbanned.");
                    banAlert.showAndWait();
                    User current = userList.findUserByUsername(user.getUserName());
                    current.setStatus("Available");
                    userListDatasource.writeData(userList);
                    statusLabel.setText("Available");
                    try {
                        FXRouter.goTo("admin");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

}
