import Entities.Customer;
import Entities.Project;
import Entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private ListView<Project> listView;

    @FXML
    TextField searchField;

    @FXML
    Button newProjectBtn;

    private ObservableList<Project> projectObservableList;

    public HomeController() {
        projectObservableList = FXCollections.observableArrayList();

        Customer customer = new Customer(
                "Jon",
                "Doe",
                "89 Gourd creek rd",
                "TX",
                "Huntsville",
                "77340",
                "9366623765",
                null,
                "jonDoe@gmail.com"
        );

        Project project  = new Project(
                "Project Cypher",
                customer,
                new User(),
                "02/21/2020",
                "Some notes about the project. This is just an example to test the length of this note.",
                Project.projectStatus.IN_PROGRESS);

        for (int i = 0; i < 20; i++) {
            projectObservableList.addAll(project);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootPane.requestFocus();
        listView.setItems(projectObservableList);
        listView.setCellFactory(projectListView -> new ProjectListItem());
    }

    public void openNewProjectForm() {
//        ControllerHandler.getInstance().setParentController(this);
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
    public void setParentController(Controller parentController) {

    }

    @Override
    public void onControllerLoadFXML(String FXML) {
        System.out.println("onControllerAction from HomeController");
    }
}













