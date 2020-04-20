import Entities.Customer;
import Entities.Project;
import Entities.Team;
import Entities.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewProjectFormController extends Controller  {

    @FXML
    private Label warning_label;

    @FXML
    private Button cancelBtn, saveProjectBtn, newTeamBtn;

    @FXML
    private TextField project_name, customer_FName, customer_LName, customer_email, customer_primary_phone,
            customer_secondary_phone, customer_st_address, customer_city, customer_zip;

    @FXML
    private ComboBox<User> project_manager;

    @FXML
    private ComboBox<Team> existing_team;

    @FXML
    private ComboBox<String> customer_state;

    @FXML
    private DatePicker start_date;

    private DatabaseManager databaseManager;

    String warning_prefix = "The following information is still missing: ";
    ObservableList<User> managers;
    ObservableList<Team> teams;
    ObservableList<String> states;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        states = FXCollections.observableArrayList(ApplicationResources.STATES);
        customer_state.setItems(states);

        customer_zip.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\D"))
                customer_zip.setText(t1.replaceAll("[^\\d]", ""));
        });

        getDatabaseInfo();
    }

    /*** retrieve data to fill in selection nodes such as the manager and teams combobox ***/
    public void getDatabaseInfo() {
        databaseManager = DatabaseManager.getInstance();
        managers = FXCollections.observableArrayList();
        teams = FXCollections.observableArrayList();

        managers.addAll(databaseManager.getManagers());
        project_manager.setItems(managers);

        teams.addAll(databaseManager.getTeams());
        existing_team.setItems(teams);
    }

    @FXML
    private void projectHandler(ActionEvent event) {
        if (event.getSource() == cancelBtn) {
            Mediator.getInstance().onControllerLoadFXML("/fxml/home.fxml");
        }
        if (event.getSource() == saveProjectBtn) {
            verifyProjectCredentials();
        }
        if (event.getSource() == newTeamBtn) {
            displayNewTeamForm();
        }
    }

    public void verifyProjectCredentials() {
        String name = project_name.getText().trim();
        String startDate = start_date.getEditor().getText();
//        User currentManager = project_manager.getValue();
        Team projectTeam = existing_team.getValue();
        String fName = customer_FName.getText().trim();
        String lName = customer_LName.getText().trim();
        String email = customer_email.getText().trim();
        String pPhone = customer_primary_phone.getText().trim();
        String sPhone = customer_secondary_phone.getText().trim();
        String stAddress = customer_st_address.getText().trim();
        String city = customer_city.getText().trim();
        String state = customer_state.getValue();

        warning_label.setText("");
        if (name.isEmpty()) {
            warning_label.setText(warning_prefix + "Project Name");
            project_name.requestFocus();
            return;
        }

        if (startDate.isEmpty()) {
            warning_label.setText(warning_prefix + "Start Date");
            start_date.requestFocus();
            return;
        }

//        if (currentManager == null) {
//            warning_label.setText(warning_prefix + "Project Manager");
//            project_manager.requestFocus();
//            return;
//        }

        if (projectTeam == null) {
            warning_label.setText(warning_prefix + "Project Team");
            existing_team.requestFocus();
            return;
        }

        if (fName.isEmpty()) {
            warning_label.setText(warning_prefix + "Customer First Name");
            customer_FName.requestFocus();
            return;
        }

        if (lName.isEmpty()) {
            warning_label.setText(warning_prefix + "Customer Last Name");
            customer_LName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            warning_label.setText(warning_prefix + "Customer Email");
            customer_email.requestFocus();
            return;
        }

        if (pPhone.isEmpty()) {
            warning_label.setText(warning_prefix + "Customer Primary Phone");
            customer_primary_phone.requestFocus();
            return;
        }

        if (stAddress.isEmpty()) {
            warning_label.setText(warning_prefix + "Customer Street Address");
            customer_st_address.requestFocus();
            return;
        }

        if (city.isEmpty()) {
            warning_label.setText(warning_prefix + "Customer City");
            customer_city.requestFocus();
            return;
        }

        if (state == null) {
            warning_label.setText(warning_prefix + "Customer State");
            customer_state.requestFocus();
            return;
        }

        if (customer_zip.getText().trim().isEmpty()) {
            warning_label.setText(warning_prefix + "Customer Zip Code");
            customer_zip.requestFocus();
            return;
        }
//        String name = project_name.getText().trim();
//        String startDate = start_date.getEditor().getText();
//        User currentManager = project_manager.getValue();
//        Team projectTeam = existing_team.getValue();
//        String fName = customer_FName.getText().trim();
//        String lName = customer_LName.getText().trim();
//        String email = customer_email.getText().trim();
//        String pPhone = customer_primary_phone.getText().trim();
//        String sPhone = customer_secondary_phone.getText().trim();
//        String stAddress = customer_st_address.getText().trim();
//        String city = customer_city.getText().trim();
//        String state = customer_state.getValue();

        int zip = Integer.parseInt(customer_zip.getText().trim());
        Customer customer = new Customer(fName, lName, email, pPhone, sPhone, stAddress, state, city, zip);
        Project project = new Project(name, startDate, projectTeam.getTID());
        databaseManager.addProject(project, customer);
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
}
