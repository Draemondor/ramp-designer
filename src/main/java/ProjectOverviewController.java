import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ProjectOverviewController extends Controller {

    @FXML
    ImageView backBtn;

    public void onClick(MouseEvent event) {
        if (event.getSource() == backBtn)
            Mediator.getInstance().onControllerLoadFXML("/fxml/home.fxml");
    }
}
