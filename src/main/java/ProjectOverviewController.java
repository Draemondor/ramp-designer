import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class ProjectOverviewController extends Controller {

    @FXML
    ImageView backBtn;

    @FXML
    Pane statusPane;

    @FXML
    Label status;

    @FXML
    Button saveBtn;

    ContextMenu statusMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusMenu = new ContextMenu();
        for (int i = 0; i < ApplicationResources.STATUS.length; i++) {
            MenuItem item = new MenuItem(ApplicationResources.STATUS[i]);
            item.setOnAction(event -> handleStatus(item));
            statusMenu.getItems().add(item);
        }
        statusPane.setOnMouseClicked(event -> statusMenu.show(statusPane, event.getScreenX(), event.getScreenY()));
    }

    private void handleStatus(MenuItem item) {
        status.setText(item.getText());
        if (item.getText().equals(ApplicationResources.STATUS[0])) {
           status.getStyleClass().clear();
           status.getStyleClass().add("IN-PLANNING");
        }
        if (item.getText().equals(ApplicationResources.STATUS[1])) {
            this.status.getStyleClass().clear();
            this.status.getStyleClass().add("IN-PROGRESS");
        }
        if (item.getText().equals(ApplicationResources.STATUS[2])) {
            this.status.getStyleClass().clear();
            this.status.getStyleClass().add("ON-HOLD");
        }
        if (item.getText().equals(ApplicationResources.STATUS[3])) {
            this.status.getStyleClass().clear();
            this.status.getStyleClass().add("CANCELED");
        }
        if (item.getText().equals(ApplicationResources.STATUS[4])) {
            this.status.getStyleClass().clear();
            this.status.getStyleClass().add("COMPLETED");
        }
    }

    public void onClick(MouseEvent event) {
        if (event.getSource() == backBtn)
            Mediator.getInstance().onControllerLoadFXML("/fxml/home.fxml");
        if (event.getSource() == saveBtn)
            ;
    }
}
