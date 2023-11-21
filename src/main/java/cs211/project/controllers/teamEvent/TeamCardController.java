package cs211.project.controllers.teamEvent;

import cs211.project.models.teamEvent.Team;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TeamCardController {
    @FXML private Label teamName;
    @FXML private Label eventName;

    private Team team;

    public void setData(Team team) {
        this.team = team;
        teamName.setText(team.getTeamName());
        eventName.setText(team.getEventName());
    }

    @FXML
    private Team onMouseClick() {
        return team;
    }
}
