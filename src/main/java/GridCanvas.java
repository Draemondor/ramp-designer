import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GridCanvas extends Pane {
    private Canvas canvas;
    private GraphicsContext gc;

    public GridCanvas(double width, double height) {
        setWidth(width); setHeight(height);
        canvas = new Canvas(width, height);
        getChildren().add(canvas);

        gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.valueOf("#FEFEFE"));
        gc.setLineWidth(0.1);

        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        gc.clearRect(0, 0, getWidth(), getHeight());

        for (int x = 40; x < getWidth(); x += 40)
            gc.strokeLine(x, 0, x, getHeight());

        for (int y = 40; y < getHeight(); y += 40)
            gc.strokeLine(0, y, getWidth(), y);
    }
}
