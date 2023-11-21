package cs211.project.controllers.teamEvent;

import cs211.project.models.teamEvent.Comment;
import cs211.project.models.user.AccountManager;
import cs211.project.models.user.User;
import cs211.project.models.user.UserList;
import cs211.project.services.Datasource;
import cs211.project.services.UserListFileDatasource;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class CommentCardController {
    @FXML private Label commentLabel;
    @FXML private Circle profile;
    @FXML private HBox root;
    @FXML private HBox comment;
    private Datasource<UserList> datasource;
    private UserList userList;

    @FXML
    private void initialize() {
        datasource = new UserListFileDatasource("data", "user-list.csv");
        userList = datasource.readData();
    }

    public void setData(Comment comment) {
        User current = AccountManager.getInstance().getCurrentUser();
        if (comment.getUsername().equals(current.getUserName())) {
            root.setAlignment(Pos.CENTER_RIGHT);
            profile.setVisible(false);
            this.comment.getStyleClass().add("bg-right");
            profile.setRadius(0);
        } else {
            this.comment.getStyleClass().add("bg-left");
        }
        commentLabel.setText(wrapText(comment.getText(), 20));
        User user = userList.findUserByUsername(comment.getUsername());
        String imgSrc = user.getImgSrc();
        Image image = new Image(imgSrc);
        profile.setFill(new ImagePattern(image));


    }

    private String wrapText(String text, int wrapLength) {
        StringBuilder wrappedText = new StringBuilder();
        int length = text.length();

        for (int i = 0; i < length; i += wrapLength) {
            int endIndex = Math.min(i + wrapLength, length);
            wrappedText.append(text, i, endIndex);

            if (endIndex < length) {
                wrappedText.append("\n");
            }
        }

        return wrappedText.toString();
    }
}
