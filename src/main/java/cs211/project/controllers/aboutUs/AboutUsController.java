package cs211.project.controllers.aboutUs;

import animatefx.animation.*;
import cs211.project.controllers.dynamicPane.DynamicPaneController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.Objects;

public class AboutUsController extends DynamicPaneController {
    @FXML private Circle moy;
    @FXML private Circle mos;
    @FXML private Circle x;
    @FXML private Circle fluke;
    @FXML private BorderPane root;

    @FXML private void initialize() throws IOException {
        root.setLeft(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/cs211/project/views/sidebar.fxml"))));

        Image puriImage = new Image(getClass().getResource("/cs211/project/images/team/puri-image.jpg").toString());
        Image mosImage = new Image(getClass().getResource("/cs211/project/images/team/pasin-image.jpg").toString());
        Image xImage = new Image(getClass().getResource("/cs211/project/images/team/prariyavit-image.jpg").toString());
        Image flukeImage = new Image(getClass().getResource("/cs211/project/images/team/yossawaj-image.jpg").toString());

        moy.setFill(new ImagePattern(puriImage));
        mos.setFill(new ImagePattern(mosImage));
        x.setFill(new ImagePattern(xImage));
        fluke.setFill(new ImagePattern(flukeImage));

        new FadeIn(root).play();
    }

}



