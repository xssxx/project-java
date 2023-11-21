package cs211.project.controllers.register;

import cs211.project.controllers.dynamicPane.DynamicPaneController;
import cs211.project.models.user.User;
import cs211.project.models.user.UserList;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserListFileDatasource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RegisterController extends DynamicPaneController {
    @FXML private TextField giveFirstName;
    @FXML private TextField giveLastName;
    @FXML private TextField giveUserName;
    @FXML private PasswordField givePassword;
    @FXML private PasswordField giveConfirmPassword;
    @FXML private ImageView showImage;
    @FXML private Label errorLabel;

    private Datasource<UserList> datasource;
    private String username;
    private String password;
    private String confirmPassword;
    private UserList userList;
    private String filename;

    @FXML
    public void initialize() {
        errorLabel.setText("");
        clearRegister();
        datasource = new UserListFileDatasource("data", "user-list.csv");
        userList = datasource.readData();
        filename = "avatars.png";
    }

    @FXML
    private void onClickSignIn() throws IOException {
        FXRouter.goTo("login");
    }

    @FXML
    private void signUp() throws IOException {
        username= giveUserName.getText();
        password = givePassword.getText();
        confirmPassword =  giveConfirmPassword.getText();
        for (User user:userList.getUsers() ){
            if (user.isUsername(username)){
                errorLabel.setText("Username already taken.");
                giveUserName.clear();
                return;
            }
        }
        if (giveFirstName.getText().isEmpty() || giveLastName.getText().isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            errorLabel.setText("Please fill in all fields.");
        } else if (!password.equals(confirmPassword)){
            errorLabel.setText("Password do not match.");
            givePassword.clear();
            giveConfirmPassword.clear();
        } else {
            userList.addUser(giveFirstName.getText(), giveLastName.getText(), username, password,filename, "user", "Available",LocalDate.now(),0, LocalDateTime.now());
            datasource.writeData(userList);
            FXRouter.goTo("login");
        }
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
                showImage.setImage(new Image(target.toUri().toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearRegister() {
        giveFirstName.setPromptText("First name");
        giveLastName.setPromptText("Last name");
        giveUserName.setPromptText("Username");
        givePassword.setPromptText("Password");
        giveConfirmPassword.setPromptText("Confirm password");
    }
}

