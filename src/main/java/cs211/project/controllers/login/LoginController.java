package cs211.project.controllers.login;

import cs211.project.controllers.dynamicPane.DynamicPaneController;
import cs211.project.models.user.User;
import cs211.project.models.user.UserList;
import cs211.project.models.user.AccountManager;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserListFileDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.time.LocalDateTime;

public class LoginController extends DynamicPaneController {
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordField;
    @FXML private Label errorLabel;
    private UserList userList;
    private Datasource<UserList> datasource;

    @FXML
    private void initialize() {
        datasource = new UserListFileDatasource("data", "user-list.csv");
        userList = datasource.readData();
        errorLabel.setText("");
    }

    @FXML
    private void onClickSignUp() throws IOException {
        FXRouter.goTo("register");
    }

    @FXML
    private void handleUserKeyEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            passwordField.requestFocus();
        }
    }

    @FXML
    private void handlePassEnterKey(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            onClickLogin();
        }
    }

    @FXML
    private void onClickLogin() throws IOException {
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        User currentUser = null;

        if(username.isEmpty() || password.isEmpty()){
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        for (User user : userList.getUsers()) {
            if (user.getUserName().equals(username)) {
                if (user.getPassword().equals(password)) {
                    currentUser = user;
                    break;
                } else {
                    errorLabel.setText("Incorrect password.");
                    return;
                }
            }
        }

        if (currentUser != null) {
            AccountManager.getInstance().setCurrentUser(currentUser);
            if (currentUser.getStatus().equals("Banned")) {
                // Create and show a pop-up alert for a banned account
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Account Banned");
                alert.setHeaderText(null);
                alert.setContentText("Your account has been banned.");

                alert.showAndWait();
                return;
            }
            // set login time
            User current = userList.findUserByUsername(currentUser.getUserName());
            current.setLatestLogin(LocalDateTime.now());
            datasource.writeData(userList);

            if (currentUser.getRole().equals("admin")) {
                FXRouter.goTo("admin");
            } else {
                FXRouter.goTo("event");
            }
        } else {
            errorLabel.setText("Username doesn't exist");
        }
    }

    @FXML
    private void onClickHelp() throws IOException {
        FXRouter.goTo("help");
    }
}
