import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import java.net.URL;
import java.util.ResourceBundle;

public class BuildController extends Controller implements EventHandler<MouseEvent> {

    @FXML
    ScrollPane center_scrollPane;

    @FXML
    StackPane center_stackPane;

    @FXML
    AnchorPane module_pane;

    @FXML
    ListView module_list;

    @FXML
    VBox vbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        center_stackPane.prefWidthProperty().bind(center_scrollPane.widthProperty());
        center_stackPane.prefHeightProperty().bind(center_scrollPane.heightProperty());
//        center_stackPane.setPrefWidth(center_scrollPane.getWidth());
//        center_stackPane.setPrefHeight(center_scrollPane.getHeight());
        DragResizer.makeResizable(module_pane, 5);
//        DragResizer.makeResizable(module_list, 0);
        setGridCanvas();
    }

    private void setGridCanvas() {
        GridCanvas gridCanvas = new GridCanvas(center_scrollPane.getWidth(), center_scrollPane.getHeight());
        center_stackPane.getChildren().add(gridCanvas);
        center_stackPane.getChildren().get(0).setStyle("-fx-background-color: #3E3D3A;");

//        center_scrollPane.widthProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue.doubleValue() - oldValue.doubleValue() < 0)
//                center_stackPane.setPrefWidth(center_stackPane.getWidth() + (oldValue.doubleValue() - newValue.doubleValue()));
////            else center_stackPane.setPrefWidth(center_stackPane.getWidth() + (newValue.doubleValue() - oldValue.doubleValue()));
//        });
//
//        center_scrollPane.heightProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue.doubleValue() - oldValue.doubleValue() < 0)
//                center_stackPane.setPrefHeight(center_stackPane.getHeight() + (oldValue.doubleValue() - newValue.doubleValue()));
////            else center_stackPane.setPrefHeight(center_stackPane.getHeight() + (newValue.doubleValue() - oldValue.doubleValue()));
//        });


        Canvas canvas = new Canvas(300, 1000);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.WHITESMOKE);
        gc.setLineWidth(0.1);

        gc.beginPath();
        gc.lineTo(200, 100);
        gc.lineTo(200, 200);
        gc.lineTo(100, 100);
        gc.stroke();
        gc.fill();

        gc.fillOval(120, 220, 100, 100);
//        gc.fillRect(120, 600, 100, 100);

        gc.fillRect(120, 350, 100, 100);

        gc.fillRoundRect(120, 500, 100, 100, 20, 20);


        vbox.getChildren().add(canvas);
    }

    @Override
    public void handle(MouseEvent event) {

    }
}
