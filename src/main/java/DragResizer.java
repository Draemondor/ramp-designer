import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class DragResizer {

    private int RESIZE_MARGIN = 5;

    private final Region region;

    private double x;

    private boolean initMinWidth;

    private boolean dragging;

    private DragResizer(Region region, int margin) {
        this.RESIZE_MARGIN = margin;
        this.region = region;
    }

    public static void makeResizable(Region region, int margin) {
        final DragResizer resizer = new DragResizer(region, margin);

        region.setOnMousePressed(resizer::mousePressed);
        region.setOnMouseDragged(resizer::mouseDragged);
        region.setOnMouseMoved(resizer::mouseOver);
        region.setOnMouseReleased(resizer::mouseReleased);
    }

    protected void mouseReleased(MouseEvent event) {
        dragging = false;
        region.setCursor(Cursor.DEFAULT);
    }

    protected void mouseOver(MouseEvent event) {
        if(isInDraggableZone(event) || dragging)
            region.setCursor(Cursor.E_RESIZE);
        else
            region.setCursor(Cursor.DEFAULT);
    }

    protected boolean isInDraggableZone(MouseEvent event) {
        return event.getX() > (region.getWidth() - RESIZE_MARGIN);
    }

    protected void mouseDragged(MouseEvent event) {
        if(!dragging)
            return;

        double mouseX = event.getX();
        double newWidth = region.getMinWidth() + (mouseX - x);
        if (newWidth <= 500) {
            region.setMinWidth(newWidth);
            x = mouseX;
        }

    }

    protected void mousePressed(MouseEvent event) {
        if(!isInDraggableZone(event))
            return;
        dragging = true;
        if (!initMinWidth) {
            region.setMinWidth(region.getWidth());
            initMinWidth = true;
        }

        x = event.getX();
    }

    public void setRESIZE_MARGIN(int RESIZE_MARGIN) {
        this.RESIZE_MARGIN = RESIZE_MARGIN;
    }
}