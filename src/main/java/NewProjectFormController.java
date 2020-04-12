import Entities.Team;
import Entities.User;
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

    private Team projectTeam;
    private User currentManager;
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
        getDatabaseInfo();
    }

    public void getDatabaseInfo() {
        databaseManager = DatabaseManager.getInstance();
        managers = FXCollections.observableArrayList();
        teams = FXCollections.observableArrayList();

        new Thread(() -> {
            managers.addAll(databaseManager.getMangers());
            project_manager.setItems(managers);
        }).start();
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
        warning_label.setText("");
        if (project_name.getText().trim().isEmpty()) {
            warning_label.setText(warning_prefix + "Project Name");
            project_name.requestFocus();
            return;
        }

        if (start_date.getEditor().getText().isEmpty()) {
            warning_label.setText(warning_prefix + "Start Date");
            start_date.requestFocus();
            return;
        }

        if (currentManager == null) {
            warning_label.setText(warning_prefix + "Project Manager");
            project_manager.requestFocus();
            return;
        }

        if (projectTeam == null) {
            warning_label.setText(warning_prefix + "Project Team");
            existing_team.requestFocus();
            return;
        }

        if (customer_FName.getText().trim().isEmpty()) {
            warning_label.setText(warning_prefix + "Customer First Name");
            customer_FName.requestFocus();
            return;
        }

        if (customer_LName.getText().trim().isEmpty()) {
            warning_label.setText(warning_prefix + "Customer Last Name");
            customer_LName.requestFocus();
            return;
        }

        if (customer_email.getText().trim().isEmpty()) {
            warning_label.setText(warning_prefix + "Customer Email");
            customer_email.requestFocus();
            return;
        }

        if (customer_primary_phone.getText().trim().isEmpty()) {
            warning_label.setText(warning_prefix + "Customer Primary Phone");
            customer_primary_phone.requestFocus();
            return;
        }

        if (customer_st_address.getText().trim().isEmpty()) {
            warning_label.setText(warning_prefix + "Customer Street Address");
            customer_st_address.requestFocus();
            return;
        }

        if (customer_city.getText().trim().isEmpty()) {
            warning_label.setText(warning_prefix + "Customer City");
            customer_city.requestFocus();
            return;
        }

        if (customer_state.getEditor().getText().isEmpty()) {
            warning_label.setText(warning_prefix + "Customer State");
            customer_state.requestFocus();
            return;
        }

        if (customer_zip.getText().trim().isEmpty()) {
            warning_label.setText(warning_prefix + "Customer Zip Code");
            customer_zip.requestFocus();
        }
    }

    public void displayNewTeamForm() {
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

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
