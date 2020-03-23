import Entities.Project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ProjectListItem extends ListCell<Project> {
    @FXML
    Label name, start_date, customer, status, note;

    @FXML
    GridPane gridPane;

    private FXMLLoader loader;

    @Override
    protected void updateItem(Project item, boolean empty) {
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
            customer.setText(item.getCustomer().getFirst_name() + " " + item.getCustomer().getLast_name());
            status.setText(item.getStatus().getStatus());
            note.setText(item.getNotes());

            setText(null);
            setGraphic(gridPane);
        }
    }
}
