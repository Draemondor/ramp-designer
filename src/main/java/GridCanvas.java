import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/*** Custom canvas class which inherits from the Pane node in order to bind certain properties for scaling as well as
     draw and render multiple objects including the grid editor to a single Node.
 ***/

public class GridCanvas extends Pane {
    private Canvas canvas;
    private GraphicsContext gc;

    public GridCanvas(double width, double height) {
        setWidth(width);
        setHeight(height);

        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        setStyle("-fx-background-color: #323232;");

        getChildren().add(canvas);

        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
    }

    @Override
    protected void layoutChildren() {

        super.layoutChildren();

        gc.setStroke(Color.valueOf("#FEFEFE"));
        gc.setLineWidth(0.1);

        gc.clearRect(0, 0, getWidth(), getHeight());

        for (int x = 40; x < this.getWidth(); x += 40)
            gc.strokeLine(x, 0,  x, this.getHeight());

        for (int y = 40; y < this.getHeight(); y += 40)
            gc.strokeLine(0, y, this.getWidth(), y);
    }
}
