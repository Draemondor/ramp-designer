import List_Items.MemberListItem;
import List_Items.TeamListItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TeamsController extends Controller {

    @FXML
    VBox items;

    @FXML
    ImageView backBtn;

    @FXML
    private ListView<TeamListItem> listView;

    @FXML
    TextField searchBar;

    @FXML
    Button newTeamBtn;

    private DatabaseManager databaseManager;
    private ObservableList<TeamListItem> teams;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseManager = DatabaseManager.getInstance();
        teams = FXCollections.observableArrayList();

        teams.addAll(databaseManager.getUserTeams());
        listView.setItems(teams);
        listView.setCellFactory(teamListView -> new UserTeamsListCell());
//        listView.setOnMouseClicked(event -> {
//            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
//                Mediator.getInstance().onControllerLoadFXML("/fxml/project_overview.fxml");
//            }
//        });
    }

    public void onClick(MouseEvent event) {
        if (event.getSource() == backBtn)
            Mediator.getInstance().onControllerLoadFXML("/fxml/home.fxml");
    }

    public void displayNewTeamForm() {
        /*** set this controller as the parent before switching views ***/
        Mediator.getInstance().setParentController(this);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/new_team_form.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setOnCloseRequest(event -> Mediator.getInstance().removeParentController());

            Stage parent = (Stage) newTeamBtn.getScene().getWindow();
            stage.initOwner(parent);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onNewTeam(ActionEvent actionEvent) {
        displayNewTeamForm();
    }

    /*** Custom recycled UI listCell for the listView to populate the users teams ***/
    static class UserTeamsListCell extends ListCell<TeamListItem> {

        @FXML
        GridPane gridPane;

        @FXML
        Label name, numProjects, numMembers, privacy;

        @FXML
        Button leaveTeam;

        private FXMLLoader loader;

        @Override
        protected void updateItem(TeamListItem item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                if (loader == null) {
                    loader = new FXMLLoader(getClass().getResource("/fxml/teams_list_item.fxml"));
                    loader.setController(this);
                    try {
                        loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                name.setText(item.getTeamName());
//                numProjects.setText(item.getTeamName());
//                numMembers.setText(item.getTeamName());
//                privacy.setText(item.getTeamName());

                setText(null);
                setGraphic(gridPane);
            }
        }

        EventHandler<ActionEvent> eventHandler = new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.getSource() == leaveTeam) {

                }
            }
        };
    }
}
