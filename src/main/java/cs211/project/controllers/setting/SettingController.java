package cs211.project.controllers.setting;

import animatefx.animation.FadeIn;
import cs211.project.controllers.dynamicPane.DynamicPaneController;
import cs211.project.models.event.Event;
import cs211.project.models.event.EventList;
import cs211.project.models.user.AccountManager;
import cs211.project.models.user.User;
import cs211.project.models.user.UserList;
import cs211.project.services.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class SettingController extends DynamicPaneController {
    @FXML private Circle showImage;
    @FXML private PasswordField oldPassword;
    @FXML private PasswordField newPassword;
    @FXML private PasswordField confirmPassword;
    @FXML private Label errorlabel;
    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;

    private UserList userList;
    private EventList eventList;
    private String filename;
    private Datasource<UserList> userListDatasource;
    private Datasource<EventList> eventListDatasource;

    @FXML private void initialize() throws IOException {
        errorlabel.setText("");
        root.setLeft(FXMLLoader.load(getClass().getResource("/cs211/project/views/sidebar.fxml")));
        User current = AccountManager.getInstance().getCurrentUser();
        Image image = new Image(current.getImgSrc());
        showImage.setFill(new ImagePattern(image));
        userListDatasource = new UserListFileDatasource("data", "user-list.csv");
        userList = userListDatasource.readData();

        eventListDatasource = new EventListFileDatasource("data/event", "event-list.csv");
        eventList = eventListDatasource.readData();

        nameLabel.setText(current.getFirstName() + " " +current.getLastName());
        usernameLabel.setText(current.getUserName());
        new FadeIn(root).play();
    }

    @FXML
    private void addImage(ActionEvent event){
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg","*.gif"));
        Node source = (Node) event.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null) {
            try {
                // CREATE FOLDER IF NOT EXIST
                File destDir = new File("data/images/profiles");
                if (!destDir.exists()) destDir.mkdirs();
                // RENAME FILE
                String[] fileSplit = file.getName().split("\\.");
                filename = LocalDate.now() + "_" + System.currentTimeMillis() + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath() + System.getProperty("file.separator") + filename
                );
                // COPY WITH FLAG REPLACE FILE IF FILE IS EXIST
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
                // SET NEW FILE PATH TO IMAGE
                Image image = new Image(target.toUri().toString());
                showImage.setFill(new ImagePattern(image));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void changeProfile() {
        User current = AccountManager.getInstance().getCurrentUser();
        User user = userList.findUserByUsername(current.getUserName());
        try {
            String imagePath = ImageReader.getImagePath("data/images/profiles", filename);
            user.setImgSrc(imagePath);
            // write image source to user list
            userListDatasource.writeData(userList);
            // write image source to events if it's creator profile
            for (Event event : eventList.getEvents()) {
                if (event.getCreatorName().equals(current.getUserName())) {
                    event.setCreatorImgSrc(imagePath);
                }
            }
            eventListDatasource.writeData(eventList);

            AccountManager.getInstance().setCurrentUser(user);
            FXRouter.goTo("event");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void changePassword() {
        if (oldPassword.getText().isEmpty() || newPassword.getText().isEmpty() || confirmPassword.getText().isEmpty()) {
            errorlabel.setText("Please fill in all fields.");
        }
        if (newPassword.getText().equals(confirmPassword.getText())) {
            User current = AccountManager.getInstance().getCurrentUser();
            User user= userList.findUserByUsername(current.getUserName());
            if (oldPassword.getText().equals(current.getPassword())) {
                user.setPassword(newPassword.getText());
                userListDatasource.writeData(userList);
                errorlabel.setText("Password Changed Successfully");
                clearSetting();
            }
        } else {
            errorlabel.setText("Password do not match");
        }
    }

    private void clearSetting() {
        oldPassword.clear();
        newPassword.clear();
        confirmPassword.clear();
    }

}

