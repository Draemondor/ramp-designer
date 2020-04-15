import Entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends Controller implements EventHandler<MouseEvent>, OnFXMLChangedListener {

    @FXML
    VBox root;

    @FXML
    BorderPane mainBorderPane;

    @FXML
    ImageView profileIcon, settingsIcon;

    @FXML
    Label userLabel;

    @FXML
    Pane  homePane, buildPane, messagePane, marketPane;

    private ObservableList<Pane> sideNavList;
    private User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sideNavList = FXCollections.observableArrayList(homePane, buildPane, messagePane, marketPane);

        Mediator.getInstance().setParentController(this);

        this.currentUser = Mediator.getInstance().getCurrentUser();

        userLabel.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        loadFXML("/fxml/home.fxml");
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() == homePane) {
            setSelectedGradient(homePane);
            loadFXML("/fxml/home.fxml");
        }
        if (event.getSource() == buildPane) {
            setSelectedGradient(buildPane);
            loadFXML("/fxml/build.fxml");
        }
//        if (event.getSource() == profileIcon) {
//            loadFXML("/fxml/profile.fxml");
//        }
//        if (event.getSource() == messagePane) {
//            setSelectedGradient(messagePane);
//            loadFXML("/fxml/message.fxml");
//        }
//        if (event.getSource() == marketPane) {
//            setSelectedGradient(marketPane);
//            loadFXML("/fxml/market.fxml");
//        }
//        if (event.getSource() == settingsIcon) {
//            loadFXML("/fxml/settings.fxml");
//        }
//        if (event.getSource() == userLabel) {
//            loadFXML("/fxml/profile.fxml");
//        }
    }

    public void setSelectedGradient(Pane pane) {
        pane.getStyleClass().clear();
        pane.getStyleClass().add("side_pane_selected");
        for (int i = 0; i < sideNavList.size(); i++) {
            if (!sideNavList.get(i).getId().equals(pane.getId())) {
                sideNavList.get(i).getStyleClass().clear();
                sideNavList.get(i).getStyleClass().add("side_pane_deselected");
            }
        }
    }

    public void loadFXML(String id) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(id));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainBorderPane.setCenter(root);
    }

    @Override
    public void onControllerLoadFXML(String FXML) {
        System.out.println("onControllerLoadFXML from MainController");
        loadFXML(FXML);
    }
}
