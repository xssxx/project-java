module cs211.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires AnimateFX;
    requires com.dlsc.gemsfx;

    opens cs211.project.cs211661project to javafx.fxml;
    exports cs211.project.cs211661project;

    // event controller
    exports cs211.project.controllers.event;
    opens cs211.project.controllers.event to javafx.fxml;

    // team controller
    exports cs211.project.controllers.aboutUs;
    opens cs211.project.controllers.aboutUs to javafx.fxml;

    // help controller
    exports cs211.project.controllers.help;
    opens cs211.project.controllers.help to javafx.fxml;

    // register controller
    exports cs211.project.controllers.register;
    opens cs211.project.controllers.register to javafx.fxml;

    // login controller
    exports cs211.project.controllers.login;
    opens cs211.project.controllers.login to javafx.fxml;

    // setting controller
    exports cs211.project.controllers.setting;
    opens cs211.project.controllers.setting to javafx.fxml;

    //admin controller
    exports cs211.project.controllers.admin;
    opens cs211.project.controllers.admin to javafx.fxml;

    // history controller
    exports cs211.project.controllers.history;
    opens cs211.project.controllers.history to javafx.fxml;

    // teamEvent controller
    exports cs211.project.controllers.teamEvent;
    opens cs211.project.controllers.teamEvent to javafx.fxml;

    // dynamicPane controller
    exports cs211.project.controllers.dynamicPane;
    opens cs211.project.controllers.dynamicPane to javafx.fxml;

    // teamEvent model
    exports cs211.project.models.teamEvent;
    opens cs211.project.models.teamEvent to javafx.fxml;

    // event model
    exports cs211.project.models.event;
    opens cs211.project.models.event to javafx.fxml;

    // user model
    exports cs211.project.models.user;
    opens cs211.project.models.user to javafx.fxml;

    // history model
    exports cs211.project.models.history;
    opens cs211.project.models.history to javafx.fxml;

    // report model
    exports cs211.project.models.report;
    opens cs211.project.models.report to javafx.fxml;
}