import Entities.User;
import List_Items.project_list_item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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
    private ListView<project_list_item> listView;

    @FXML
    TextField searchField;

    @FXML
    Button newProjectBtn, teamsBtn;

    private DatabaseManager databaseManager;
    private ObservableList<project_list_item> projectObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getProjects();
    }

    public void getProjects() {
        databaseManager = DatabaseManager.getInstance();
        projectObservableList = FXCollections.observableArrayList();

        new Thread(() -> {
            projectObservableList.addAll(databaseManager.getProjects());
            listView.setItems(projectObservableList);
            listView.setCellFactory(projectListView -> new ProjectListItem());
        }).start();
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
}













