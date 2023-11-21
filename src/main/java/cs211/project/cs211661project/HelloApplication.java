package cs211.project.cs211661project;

import javafx.application.Application;
import javafx.stage.Stage;
import cs211.project.services.FXRouter;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException{
        FXRouter.bind(this, stage, "Kirby Event Manager", 800, 600);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        configRoute();
        FXRouter.goTo("login");
    }

    private static void configRoute() {
        String resourcesPath = "cs211/project/views/";

        FXRouter.when("event", resourcesPath + "events/event-page.fxml");
        FXRouter.when("edit-event-list", resourcesPath + "events/edit-event-list.fxml");
        FXRouter.when("edit-event", resourcesPath + "events/edit-event.fxml");
        FXRouter.when("create-event", resourcesPath + "events/create-event.fxml");
        FXRouter.when("event-detail", resourcesPath + "events/event-detail.fxml");
        FXRouter.when("event-member-list", resourcesPath + "events/event-member-list.fxml");

        FXRouter.when("help", resourcesPath + "help.fxml");

        FXRouter.when("login", resourcesPath + "login-view.fxml");
        FXRouter.when("register",resourcesPath + "register-view.fxml");
        FXRouter.when("setting",resourcesPath + "setting-view.fxml");
        FXRouter.when("admin",resourcesPath + "list-admin-view.fxml");
        FXRouter.when("admin-user",resourcesPath + "admin-user-view.fxml");
        FXRouter.when("history",resourcesPath + "history-event.fxml");

        FXRouter.when("about-us",  resourcesPath + "about-us.fxml");
        FXRouter.when("team-event", resourcesPath + "teams/team-comment.fxml");
        FXRouter.when("team-event-edit", resourcesPath + "teams/team-event-edit.fxml");
        FXRouter.when("team-event-management", resourcesPath + "teams/team-event-management.fxml");
        FXRouter.when("team-event-add", resourcesPath + "teams/team-event-add.fxml");
        FXRouter.when("team-event-create", resourcesPath + "teams/team-event-create.fxml");

    }

    public static void main(String[] args) {
        launch();
    }

}