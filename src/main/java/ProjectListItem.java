import List_Items.project_list_item;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ProjectListItem extends ListCell<project_list_item> {

    @FXML
    Label name, start_date, customer, status, note, team;

    @FXML
    GridPane gridPane;

    private FXMLLoader loader;

    @Override
    protected void updateItem(project_list_item item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("/fxml/list_item.fxml"));
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









