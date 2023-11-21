package cs211.project.controllers.admin;

import cs211.project.models.user.User;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserCardController {
    @FXML private Rectangle userImage;
    @FXML private Label userLabel;
    @FXML private Label fnameLabel;
    @FXML private Label lnameLabel;
    @FXML private Label reportLabel;
    @FXML private Label latestLoginLabel;
    private User user;

    public void setData(User user) {
        fnameLabel.setText(user.getFirstName());
        lnameLabel.setText(user.getLastName());
        userLabel.setText(user.getUserName());
        reportLabel.setText(Integer.toString(user.getReportCount()));
        Image image = new Image(user.getImgSrc());
        userImage.setFill(new ImagePattern(image));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime latestLoginDateTime = user.getLatestLogin();
        String formattedLatestLogin = latestLoginDateTime.format(dateFormatter);
        latestLoginLabel.setText(formattedLatestLogin);

        this.user = user;
    }

    @FXML
    private void goUser() throws IOException {
        FXRouter.goTo("admin-user", user);
    }

}
