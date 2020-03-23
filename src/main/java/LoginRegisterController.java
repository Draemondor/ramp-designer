import Entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class LoginRegisterController extends Controller implements OnFXMLChangedListener {

    @FXML
    private Button login_btn, signup_btn, create_account_btn;

    @FXML
    private TextField email_field, first_name, last_name, email, password, confirmed_password, phone_number;

    @FXML
    PasswordField password_field;

    @FXML
    private Label forgot_password_btn, login_redirect;

    @FXML
    private AnchorPane login_pane, register_pane;

    @FXML
    private CheckBox remember_user;

    @Override
    public void setParentController(Controller parentController) {  }

    @Override
    public void onControllerLoadFXML(String FXML) {  }

    public void onClick(ActionEvent event) {
        if (event.getSource() == signup_btn)
            register_pane.toFront();
        if (event.getSource() == login_btn)
            verifyLoginCredentials();
        if (event.getSource() == create_account_btn)
            verifyRegisteredCredentials();
    }

    public void onViewClicked(MouseEvent event) {
        if (event.getSource() == login_redirect)
            login_pane.toFront();
    }

    public void verifyLoginCredentials() {
        String email, password;
        email = email_field.getText().trim();
        password = password_field.getText().trim();

        if (email.isEmpty()) {
            email_field.requestFocus();
            AlertClass.displayAlert(Alert.AlertType.WARNING, "Enter a valid email address.");
            return;
        }

        if (password.isEmpty()) {
            password_field.requestFocus();
            AlertClass.displayAlert(Alert.AlertType.WARNING, "Enter your password.");
            return;
        }

        User currentUser = DatabaseManager.loginCredentialsVerified(email, password);
        if (currentUser != null)
            loadUserSession(currentUser);
        else
            AlertClass.displayAlert(Alert.AlertType.ERROR, "The information you entered did not match any of our records.");
    }

    public void verifyRegisteredCredentials() {
        String FName, LName, mEmail, mPassword, confirmedPassword, phone;
        FName = first_name.getText().trim();
        LName = last_name.getText().trim();
        mEmail = email.getText().trim();
        mPassword = password.getText().trim();
        confirmedPassword = confirmed_password.getText().trim();
        phone = phone_number.getText().trim();

        if (FName.isEmpty()) {
            first_name.requestFocus();
            AlertClass.displayAlert(Alert.AlertType.WARNING, "Enter your first name.");
            return;
        }

        if (LName.isEmpty()) {
            last_name.requestFocus();
            AlertClass.displayAlert(Alert.AlertType.WARNING, "Enter your last name.");
            return;
        }

        if (mEmail.isEmpty()) {
            email.requestFocus();
            AlertClass.displayAlert(Alert.AlertType.WARNING, "Enter a valid email address.");
            return;
        }

        if (phone.isEmpty()) {
            phone_number.requestFocus();
            AlertClass.displayAlert(Alert.AlertType.WARNING, "Enter a valid phone number.");
            return;
        }

        if (mPassword.isEmpty()) {
            password.requestFocus();
            AlertClass.displayAlert(Alert.AlertType.WARNING, "Enter a a valid password for your account.");
            return;
        }

        if (confirmedPassword.isEmpty()) {
            confirmed_password.requestFocus();
            AlertClass.displayAlert(Alert.AlertType.WARNING, "Confirm your password.");
            return;
        }

        if (!mPassword.equals(confirmedPassword)) {
            AlertClass.displayAlert(Alert.AlertType.WARNING, "Passwords do not match.");
            return;
        }

        if (mPassword.length() < 6) {
            password.requestFocus();
            AlertClass.displayAlert(Alert.AlertType.WARNING, "Password must be at least 6 characters");
            return;
        }

        if (!DatabaseManager.isEmailTaken(mEmail)) {
            String hash = BCrypt.hashpw(mPassword, BCrypt.gensalt());
            User newUser = new User(FName, LName, mEmail, hash, phone);
            User currentUser = DatabaseManager.addUser(newUser);
            if (currentUser != null)
                loadUserSession(currentUser);
            else AlertClass.displayAlert(Alert.AlertType.ERROR, "There was a problem when trying to register.");
        } else {
            email.requestFocus();
            AlertClass.displayAlert(Alert.AlertType.WARNING, "There is already an account registered with this email address.");
        }
    }

    public void loadUserSession(User currentUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();
            mainController.setCurrentUser(currentUser);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);

            stage.show();
            signup_btn.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



















