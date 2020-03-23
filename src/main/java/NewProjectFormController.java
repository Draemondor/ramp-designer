import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class NewProjectFormController extends Controller implements EventHandler<MouseEvent>  {

    @FXML
    Pane schemeLightBlue, schemeBlue, schemeGreen, schemeOrange, schemeRed;

    @FXML
    Button cancelBtn, saveProjectBtn, newTeamBtn;

    @FXML
    TextField project_name, customer_FName, customer_LName, customer_email, customer_primary_phone,
            customer_secondary_phone, customer_st_address, customer_city, customer_zip;

    @FXML
    ComboBox project_manager, existing_team, customer_state;

    @FXML
    DatePicker start_date;

    ObservableList<Pane> newProjectColorScheme;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newProjectColorScheme = FXCollections.observableArrayList(schemeLightBlue, schemeBlue, schemeGreen, schemeOrange, schemeRed);
    }

    @FXML
    private void projectHandler(ActionEvent event) {
        if (event.getSource() == cancelBtn) {
            ControllerHandler.getInstance().onControllerLoadFXML("/fxml/home.fxml");
        }
        if (event.getSource() == saveProjectBtn) {
            System.out.println("save");
        }
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() == schemeLightBlue)
            setSelectedProjectColorScheme(schemeLightBlue);
        if (event.getSource() == schemeBlue)
            setSelectedProjectColorScheme(schemeBlue);
        if (event.getSource() == schemeGreen)
            setSelectedProjectColorScheme(schemeGreen);
        if (event.getSource() == schemeOrange)
            setSelectedProjectColorScheme(schemeOrange);
        if (event.getSource() == schemeRed)
            setSelectedProjectColorScheme(schemeRed);
    }

    public void setSelectedProjectColorScheme(Pane colorScheme) {
        System.out.println(colorScheme.getId());
        colorScheme.getStyleClass().clear();
        colorScheme.getStyleClass().add("new_project_color_scheme_selected");
        for (int i = 0; i < newProjectColorScheme.size(); i++) {
            if (!newProjectColorScheme.get(i).getId().equals(colorScheme.getId())) {
                newProjectColorScheme.get(i).getStyleClass().clear();
                newProjectColorScheme.get(i).getStyleClass().add("new_project_color_scheme_deselected");
            }
        }
    }
}
