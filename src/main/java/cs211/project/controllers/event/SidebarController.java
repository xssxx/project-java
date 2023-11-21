package cs211.project.controllers.event;

import cs211.project.models.user.User;
import cs211.project.models.user.AccountManager;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class SidebarController {
    @FXML
    private HBox adminBox;
    @FXML
    private void initialize() {
        User currentUser = AccountManager.getInstance().getCurrentUser();
        if (currentUser.getRole().equals("user")) {
            adminBox.setVisible(false);
            adminBox.setDisable(true);
        }
    }
    @FXML
    private void onLogout() throws IOException {
        FXRouter.goTo("login");

    }

    @FXML
    private void onAboutUs() throws IOException {
        FXRouter.goTo("about-us");
    }

    @FXML
    private void onHome() throws IOException {
        FXRouter.goTo("event");
    }

    @FXML
    private void onHistory() throws IOException {
        FXRouter.goTo("history");
    }

    @FXML
    private void onCreateEvent() throws IOException {
        FXRouter.goTo("create-event");
    }

    @FXML
    private void onEditList() throws IOException {
        FXRouter.goTo("edit-event-list");
    }

    @FXML
    private void onMyTeam() throws IOException {
        FXRouter.goTo("team-event");
    }

    @FXML
    private void onClickUser() throws IOException {
        FXRouter.goTo("setting");
    }

    @FXML
    private void onClickAdmin() throws IOException {
        User currentUser = AccountManager.getInstance().getCurrentUser();
        if (currentUser.getRole().equals("admin")) {
            FXRouter.goTo("admin");
        }
    }

}
