import Entities.Project;
import Entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import static java.util.Map.entry;

public class NewTeamFormController extends Controller implements OnFXMLChangedListener {
    public enum TEAM_MEMBER_TYPE {Manager, Leader, Member}

    @FXML
    private Button cancelBtn, createBtn;

    @FXML
    private TextField team_name;

    @FXML
    private ComboBox<String> privacy_lvl;

    @FXML
    private Label mngr_name, mngr_email, mngr_phone, privacy_note;

    @FXML
    private AnchorPane add_manager, add_leader, add_member;


    private User currentUser;

//    @FXML
//    AnchorPane schemeLightBlue, schemeBlue, schemeGreen, schemeOrange, schemeRed;
//
//    ObservableList<AnchorPane> teamColorScheme;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        teamColorScheme = FXCollections.observableArrayList(schemeLightBlue, schemeBlue, schemeGreen, schemeOrange, schemeRed);
        currentUser = Mediator.getInstance().getCurrentUser();
        setFields();
    }

//    public void handle(MouseEvent event) {
//        if (event.getSource() == schemeLightBlue)
//            setSelectedProjectColorScheme(schemeLightBlue);
//        if (event.getSource() == schemeBlue)
//            setSelectedProjectColorScheme(schemeBlue);
//        if (event.getSource() == schemeGreen)
//            setSelectedProjectColorScheme(schemeGreen);
//        if (event.getSource() == schemeOrange)
//            setSelectedProjectColorScheme(schemeOrange);
//        if (event.getSource() == schemeRed)
//            setSelectedProjectColorScheme(schemeRed);
//    }
//
//    public void setSelectedProjectColorScheme(AnchorPane colorScheme) {
//        colorScheme.getStyleClass().clear();
//        colorScheme.getStyleClass().add("new_project_color_scheme_selected");
//        for (AnchorPane anchorPane : teamColorScheme) {
//            if (!anchorPane.getId().equals(colorScheme.getId())) {
//                anchorPane.getStyleClass().clear();
//                anchorPane.getStyleClass().add("new_project_color_scheme_deselected");
//            }
//        }
//    }

    public void setFields() {
        mngr_name.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        mngr_email.setText(currentUser.getEmail());
        mngr_phone.setText(currentUser.getPhone());

        privacy_lvl.getItems().addAll(FXCollections.observableArrayList(ApplicationResources.TEAM_PRIVACY_LEVEL));
        privacy_lvl.getSelectionModel().selectFirst();
        privacy_note.setText("Anyone can join this team.");
    }

    public void onPrivacySelected(ActionEvent event) {
        if (event.getSource() == privacy_lvl) {
            switch (privacy_lvl.getSelectionModel().getSelectedIndex()) {
                case(0):
                    privacy_note.setText("Anyone can join this team.");
                    break;
                case(1):
                    privacy_note.setText("Any active team member can add other members to this team.");
                    break;
                case(2):
                    privacy_note.setText("Only managers or team leaders can add other members to this team.");
                    break;
            }
        }
    }

    public void onAdd(MouseEvent event) {
        if (event.getSource() == add_manager) selectFromUserList(TEAM_MEMBER_TYPE.Manager);
        if (event.getSource() == add_leader) selectFromUserList(TEAM_MEMBER_TYPE.Leader);
        if (event.getSource() == add_member)selectFromUserList(TEAM_MEMBER_TYPE.Member);
    }

    public void selectFromUserList(TEAM_MEMBER_TYPE member_type) {
        switch (member_type) {
            case Manager:
                break;
            case Leader:
                break;
            case Member:
                break;
        }
    }

    public void onClick(ActionEvent event) {
        if (event.getSource() == cancelBtn) {
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            stage.close();
        }

        if (event.getSource() == createBtn) {
            System.out.println("create");
        }
    }

    @Override
    public void onControllerLoadFXML(String FXML) {
        System.out.println("onControllerLoadFXML from NewTeamFormController");

    }
}
