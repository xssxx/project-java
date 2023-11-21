package cs211.project.controllers.help;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;

import java.io.IOException;

public class helpController {
    @FXML
    private void onClickBack() throws IOException {
        FXRouter.goTo("login");
    }
}
