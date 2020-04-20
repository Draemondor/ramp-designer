import List_Items.ProjectListItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController extends Controller implements OnFXMLChangedListener {

    @FXML
    BorderPane rootPane;

    @FXML
    VBox items;

    @FXML
    private ListView<ProjectListItem> listView;

    @FXML
    TextField searchField;

    @FXML
    Button newProjectBtn, teamsBtn;

    private DatabaseManager databaseManager;
    private ObservableList<ProjectListItem> projectObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getProjects();
    }

    public void getProjects() {
        databaseManager = DatabaseManager.getInstance();
        projectObservableList = FXCollections.observableArrayList();

        /*** retrieve projects from the database and populate the listView. ***/
        projectObservableList.addAll(databaseManager.getAllProjects());
        listView.setItems(projectObservableList);
        listView.setCellFactory(projectListView -> new ProjectListCell());
        listView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                Mediator.getInstance().onControllerLoadFXML("/fxml/project_overview.fxml");
            }
        });
    }

    public void openNewProjectForm() {
        rootPane.setTop(null);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/new_project_form.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        rootPane.setCenter(root);
    }

    @Override
    public void onControllerLoadFXML(String FXML) {
        System.out.println("onControllerLoadFXML from HomeController");
    }

    public void onAction(ActionEvent actionEvent) {

    }

    /*** Custom recycled UI listCell for the listView to populate projects ***/
    static class ProjectListCell extends ListCell<ProjectListItem> {

        @FXML
        Label name, start_date, customer, status, note, team;

        @FXML
        GridPane gridPane;

        private FXMLLoader loader;

        @Override
        protected void updateItem(ProjectListItem item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                if (loader == null) {
                    loader = new FXMLLoader(getClass().getResource("/fxml/project_list_item.fxml"));
                    loader.setController(this);
                    try {
                        loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                name.setText(item.getProject_name());
                start_date.setText(item.getStart_date());
                customer.setText(item.getCustomer());
                status.setText(ApplicationResources.STATUS[item.getStatus()]);
                setStatusColor(item.getStatus());
                team.setText(item.getTeam());
                note.setText(item.getNotes());

                setText(null);
                setGraphic(gridPane);
            }
        }

        private void setStatusColor(int status) {
            switch (status) {
                case(0):
                    this.status.getStyleClass().clear();
                    this.status.getStyleClass().add("IN-PLANNING");
                    break;

                case(1):
                    this.status.getStyleClass().clear();
                    this.status.getStyleClass().add("IN-PROGRESS");
                    break;

                case(2):
                    this.status.getStyleClass().clear();
                    this.status.getStyleClass().add("ON-HOLD");
                    break;

                case(3):
                    this.status.getStyleClass().clear();
                    this.status.getStyleClass().add("CANCELED");
                    break;

                case(4):
                    this.status.getStyleClass().clear();
                    this.status.getStyleClass().add("COMPLETED");
                    break;
            }
        }
    }
}













