import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        DatabaseManager manager = DatabaseManager.getInstance();
        manager.getConnection();
        manager.createDefaultTables();

        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/loginRegister.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
//        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
    }
}
