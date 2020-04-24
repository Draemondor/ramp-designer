import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import javax.naming.Binding;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class BuildController extends Controller {

    @FXML
    AnchorPane anchorPane;

    @FXML
    ToggleButton point, line;

    @FXML
    BorderPane borderPane;

    @FXML
    Button circle, square, triangle, module, clear;

    @FXML
    TextField x, y;

    @FXML
    ImageView refresh, delete;

    SubScene subScene;
    GridCanvas gridCanvas;
    ToggleGroup toggleGroup;

    DecimalFormat df = new DecimalFormat("#,###,##0");
    boolean isPointEnabled, isLineEnabled;
    Line currentLine;
    Node currentNode;
    Circle currentMarker;

    /***  Positioning variables used to move items around the screen relative to the mouse position and current Node translation.
     Used with the EventHandlers.
     ***/

    double relativeMouseX;
    double relativeMouseY;
    double relativeTranslateX;
    double relativeTranslateY;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toggleGroup = new ToggleGroup();
        point.setToggleGroup(toggleGroup);
        line.setToggleGroup(toggleGroup);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                isPointEnabled = false;
                isLineEnabled = false;
            }
            else if (toggleGroup.getSelectedToggle() == point) {
                isPointEnabled = true;
                isLineEnabled = false;
            }
            else if (toggleGroup.getSelectedToggle() == line) {
                isPointEnabled = false;
                isLineEnabled = true;
            }
        });

        gridCanvas = new GridCanvas(900, 550);
        subScene = new SubScene(gridCanvas, 1200, 900);
        anchorPane.getChildren().add(subScene);

        /*** Binding of the two Node's width and height property for automatic scaling ***/
        subScene.widthProperty().bind(anchorPane.widthProperty());
        subScene.heightProperty().bind(anchorPane.heightProperty());
        gridCanvas.prefWidthProperty().bind(anchorPane.widthProperty());
        gridCanvas.prefHeightProperty().bind(anchorPane.heightProperty());

        subScene.setOnMouseMoved(e -> {
            if (isLineEnabled && currentLine != null) {
                currentLine.setEndX(e.getX());
                currentLine.setEndY(e.getY());
            }
        });

        subScene.setOnMouseClicked(e -> {
            if (currentMarker != null) {
                currentMarker.setStroke(Color.valueOf("#FF8C00"));
                currentMarker.setFill(Color.valueOf("#FF8C00").deriveColor(1, 1, 1, 0.5));
            }
            this.x.clear(); this.y.clear();
            if (e.getButton() == MouseButton.PRIMARY) {
                if (e.getClickCount() == 1) {
                    if (isPointEnabled) drawPoint();
                }
                else if (e.getClickCount() == 2) {
                    Point2D center = gridCanvas.sceneToLocal(new Point2D(e.getX(), e.getY()));
                    Circle marker = new Circle(center.getX(), center.getY(), 5);
                    marker.setStroke(Color.GREENYELLOW);
                    marker.setFill(Color.GREENYELLOW);
                    currentMarker = marker;
                    gridCanvas.getChildren().add(marker);
                    currentNode = currentMarker;

                    if (isLineEnabled) {
                        Line newLine = new Line(marker.getCenterX(), marker.getCenterY(), marker.getCenterX(), marker.getCenterY());
                        newLine.setStroke(Color.AQUA);
                        currentLine = newLine;
                        gridCanvas.getChildren().add(currentLine);
                        currentNode = currentMarker;
                    }

                    this.x.setText(df.format(marker.getCenterX()));
                    this.y.setText(df.format(marker.getCenterY()));

                    addMarkerEventHandler(marker);
                }
                e.consume();
            }
            else if (e.getButton() == MouseButton.SECONDARY) {
                if (isLineEnabled)
                    gridCanvas.getChildren().remove(currentLine);
            }
        });
    }

    public void addMarkerEventHandler(Circle marker) {
        marker.setOnMouseClicked(event -> {
                currentNode = marker;
            if (event.getButton() == MouseButton.PRIMARY) {
                currentMarker.setStroke(Color.valueOf("#FF8C00"));
                currentMarker.setFill(Color.valueOf("#FF8C00").deriveColor(1, 1, 1, 0.5));
                currentMarker = marker;
                currentMarker.setStroke(Color.GREENYELLOW);
                currentMarker.setFill(Color.GREENYELLOW);
                this.x.setText(df.format(marker.getCenterX()));
                this.y.setText(df.format(marker.getCenterY()));

                if (isLineEnabled) {
                    Line newLine = new Line(marker.getCenterX(), marker.getCenterY(), marker.getCenterX(), marker.getCenterY());
                    newLine.setStroke(Color.AQUA);
                    currentLine = newLine;
                    gridCanvas.getChildren().add(currentLine);
                }
            }
            else if (event.getButton() == MouseButton.SECONDARY) {
                if (event.getClickCount() == 2) {
                    gridCanvas.getChildren().remove(marker);
                    this.x.clear(); this.y.clear();
                }
            }
            event.consume();
        });
    }

    public void onClick(ActionEvent event) {
        if (event.getSource() == circle) drawCircle();
        if (event.getSource() == square) drawRectangle();
        if (event.getSource() == triangle) drawTriangle();
        if (event.getSource() == module) drawModule();
        if (event.getSource() == clear) {
            gridCanvas.clear();
            this.x.clear(); this.y.clear();
        }
        if (event.getSource() == refresh) refreshView();
    }

    public void onAltAction(MouseEvent event) {
        if (event.getSource() == delete && currentNode != null) {
            gridCanvas.getChildren().remove(currentNode);
            if (gridCanvas.getChildren().size() > 1)
                currentNode = gridCanvas.getChildren().get(gridCanvas.getChildren().size() - 1);
        }
    }

    @FXML
    public void onEnter(ActionEvent actionEvent) {
        refreshView();
    }

    public void refreshView() {
        if (currentMarker != null) {
            if (!x.getText().trim().isEmpty())
                currentMarker.setCenterX(Double.parseDouble(x.getText().trim()));
            if (!y.getText().trim().isEmpty())
                currentMarker.setCenterY(Double.parseDouble(y.getText().trim()));
        }
    }

    public void drawCircle() {
        Circle circle = new Circle( 80, 80, 40);
        currentNode = circle;
        circle.setStroke(Color.AQUA);
        circle.setFill(Color.GRAY.deriveColor(1, 1, 1, 0.5));
        addObjectEventHandler(circle);
        gridCanvas.getChildren().addAll(circle);
    }

    public void drawRectangle() {
        Rectangle rectangle = new Rectangle(80,80);
        currentNode = rectangle;
        rectangle.setTranslateX(80);
        rectangle.setTranslateY(80);
        rectangle.setStroke(Color.AQUA);
        rectangle.setFill(Color.GRAY.deriveColor(1, 1, 1, 0.5));
        addObjectEventHandler(rectangle);
        gridCanvas.getChildren().addAll(rectangle);
    }

    public void drawTriangle() {
        Polygon triangle = new Polygon();
        currentNode = triangle;
        triangle.getPoints().addAll(160.0, 40.0, 120.0, 120.0, 200.0, 120.0);
        triangle.setStroke(Color.AQUA);
        triangle.setFill(Color.GRAY.deriveColor(1, 1, 1, 0.5));
        addObjectEventHandler(triangle);
        gridCanvas.getChildren().add(triangle);
    }

    private void drawModule() {
        Group group = new Group();
        currentNode = group;

        Polygon top = new Polygon();
        top.getPoints().addAll(160.0, 160.0, 400.0, 200.0, 440.0, 160.0, 200.0, 120.0);
        top.setStroke(Color.AQUA);
        setFillProperty(top);

        Polygon base = new Polygon();
        base.getPoints().addAll(160.0, 160.0, 160.0, 200.0, 400.0, 220.0, 400.0, 200.0);
        base.setStroke(Color.AQUA);
        setFillProperty(base);

        Polygon connector_top = new Polygon();
        connector_top.getPoints().addAll(400.0, 200.0, 480.0, 200.0, 520.0, 160.0, 440.0, 160.0);
        connector_top.setStroke(Color.AQUA);
        setFillProperty(connector_top);

        Polygon connector_left = new Polygon();
        connector_left.getPoints().addAll(400.0, 200.0, 400.0, 220.0, 480.0, 220.0, 480.0, 200.0);
        connector_left.setStroke(Color.AQUA);
        setFillProperty(connector_left);

        Polygon connector_right = new Polygon();
        connector_right.getPoints().addAll(480.0, 200.0, 480.0, 220.0, 520.0, 180.0, 520.0, 160.0);
        connector_right.setStroke(Color.AQUA);
        setFillProperty(connector_right);

        Polygon bottom_ramp_side = new Polygon();
        bottom_ramp_side.getPoints().addAll(520.0, 160.0, 520.0, 180.0, 680.0, 80.0);
        bottom_ramp_side.setStroke(Color.AQUA);
        setFillProperty(bottom_ramp_side);

        Polygon bottom_ramp_top = new Polygon();
        bottom_ramp_top.getPoints().addAll(440.0, 160.0, 520.0, 160.0, 680.0, 80.0, 600.0, 80.0);
        bottom_ramp_top.setStroke(Color.AQUA);
        setFillProperty(bottom_ramp_top);

        group.getChildren().addAll(top, base, connector_top, connector_left, connector_right, bottom_ramp_side, bottom_ramp_top);
        addObjectEventHandler(group);
        gridCanvas.getChildren().add(group);
    }

    public void setFillProperty(Shape shape) {
        shape.fillProperty().bind(Bindings.when(shape.hoverProperty())
                .then(Color.GOLD.deriveColor(1, 1, 1, 0.5))
                .otherwise(Color.GRAY.deriveColor(1, 1, 1, 0.5)));
    }

    public void drawPoint() {
        Circle circle = new Circle( 80, 80, 5);
        currentNode = circle;
        circle.setStroke(Color.RED);
        circle.setFill(Color.RED.deriveColor(1, 1, 1, 0.5));
        addObjectEventHandler(circle);
        gridCanvas.getChildren().addAll(circle);
    }

    public void addObjectEventHandler(Node node) {
        node.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (!event.isPrimaryButtonDown())
                return;

            relativeMouseX = event.getSceneX();
            relativeMouseY = event.getSceneY();

            relativeTranslateX = ((Node) event.getSource()).getTranslateX();
            relativeTranslateY = ((Node) event.getSource()).getTranslateY();

        });

        node.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            this.x.clear(); this.y.clear();
            if (!event.isPrimaryButtonDown())
                return;

            Node eventNode = (Node) event.getSource();

            if (gridCanvas.getLayoutX() >= 0) {
                eventNode.setTranslateX(relativeTranslateX + ((event.getSceneX() - relativeMouseX)));
                eventNode.setTranslateY(relativeTranslateY + ((event.getSceneY() - relativeMouseY)));
//
//                this.x.setText(df.format(eventNode.getTranslateX() + 80));
//                this.y.setText(df.format(eventNode.getTranslateY() + 80));

                event.consume();
            }
            else System.out.println("OUT OF BOUNDS");

        });

        node.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            this.currentNode = node;
            if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 2)
                gridCanvas.getChildren().remove(node);
        });
    }
}
