
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class BuildController extends Controller {

    @FXML
    AnchorPane anchorPane;

    @FXML
    BorderPane borderPane;

    @FXML
    Button circle, square, triangle, section;

    SubScene subScene;
    GridCanvas gridCanvas;

    double relativeMouseX;
    double relativeMouseY;
    double relativeTranslateX;
    double relativeTranslateY;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gridCanvas = new GridCanvas(900, 550);
        subScene = new SubScene(gridCanvas, 1200, 900);
        anchorPane.getChildren().add(subScene);

        subScene.widthProperty().bind(anchorPane.widthProperty());
        subScene.heightProperty().bind(anchorPane.heightProperty());
        gridCanvas.prefWidthProperty().bind(anchorPane.widthProperty());
        gridCanvas.prefHeightProperty().bind(anchorPane.heightProperty());
    }

    public void onClick(ActionEvent event) {
        if (event.getSource() == circle)
            drawCircle(gridCanvas);
        if (event.getSource() == square)
            drawRectangle(gridCanvas);
        if (event.getSource() == triangle)
            drawTriangle(gridCanvas);
        if (event.getSource() == section)
            createSection(gridCanvas);
    }

    public void drawCircle(GridCanvas gridCanvas) {
        Circle circle = new Circle( 80, 80, 40);
        circle.setStroke(Color.GOLD);
        circle.setFill(Color.GOLD.deriveColor(1, 1, 1, 0.5));
        addObjectEventHandler(circle);
        gridCanvas.getChildren().addAll(circle);
    }

    public void drawRectangle(GridCanvas gridCanvas) {
        Rectangle rectangle = new Rectangle(80,80);
        rectangle.setTranslateX(80);
        rectangle.setTranslateY(80);
        rectangle.setStroke(Color.AQUA);
        rectangle.setFill(Color.AQUA.deriveColor(1, 1, 1, 0.5));
        addObjectEventHandler(rectangle);
        gridCanvas.getChildren().addAll(rectangle);
    }

    public void drawTriangle(GridCanvas gridCanvas) {
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(
                160.0, 40.0,
                120.0, 120.0,
                200.0, 120.0);
        triangle.setStroke(Color.GREEN);
        triangle.setFill(Color.GREEN.deriveColor(1, 1, 1, 0.5));
        addObjectEventHandler(triangle);
        gridCanvas.getChildren().add(triangle);
    }

    public void createSection(GridCanvas gridCanvas) {

    }

    public void addObjectEventHandler(Node node) {
        node.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if(!event.isPrimaryButtonDown())
                return;

            relativeMouseX = event.getSceneX();
            relativeMouseY = event.getSceneY();

            relativeTranslateX = ((Node) event.getSource()).getTranslateX();
            relativeTranslateY = ((Node) event.getSource()).getTranslateY();
        });

        node.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if(!event.isPrimaryButtonDown())
                return;

            Node eventNode = (Node) event.getSource();

            if (gridCanvas.getLayoutX() >= 0) {
                eventNode.setTranslateX(relativeTranslateX + ((event.getSceneX() - relativeMouseX)) );
                eventNode.setTranslateY(relativeTranslateY + ((event.getSceneY() - relativeMouseY)) );
                event.consume();
            } else System.out.println("OUT OF BOUNDS");

        });
    }
}
