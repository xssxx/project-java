package cs211.project.controllers.dynamicPane;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DynamicPaneController {
    @FXML protected BorderPane root;
    @FXML protected AnchorPane anchorPane;
    @FXML protected Label closeButton;
    @FXML protected Label hideButton;

    protected double x = 0;
    protected double y = 0;

    @FXML
    protected void closeButtonAction() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void hideButtonAction() {
        Stage stage = (Stage) hideButton.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    protected void borderPaneDragged(MouseEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setY(event.getScreenY() - y);
        stage.setX(event.getScreenX() - x);
    }

    @FXML
    protected void borderPanePressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    protected void anchorPaneDragged(MouseEvent event){
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setY(event.getScreenY() - y);
        stage.setX(event.getScreenX() - x);
    }

    @FXML
    protected void anchorPanePressed(MouseEvent event){
        x = event.getSceneX();
        y = event.getSceneY();
    }
}
