import javafx.scene.control.Alert;

public class AlertClass {

    public static void displayAlert(Alert.AlertType alertType, String alert_message) {
        Alert alert = new Alert(alertType, alert_message);
        alert.show();
    }
}
