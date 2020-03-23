import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.ResourceBundle;

public class BuildController extends Controller implements EventHandler<MouseEvent> {

    @FXML
    Pane pane1, pane2, pane3;

    @FXML
    Label dim, obs, other;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dim.prefWidthProperty().bind(pane1.widthProperty());
        obs.prefWidthProperty().bind(pane2.widthProperty());
        other.prefWidthProperty().bind(pane3.widthProperty());
    }

    @Override
    public void handle(MouseEvent event) {

    }
}
