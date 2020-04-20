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
import java.net.URL;
import java.util.ResourceBundle;

public class LoginRegisterController extends Controller {

    @FXML
    private Button login_btn, signup_btn, create_account_btn;

    @FXML
    private TextField email_field, first_name, last_name, email, password, confirmed_password, phone_number;

    @FXML
    PasswordField password_field;

    @FXML
    private Label forgot_password_btn, login_redirect, login_warning_label, register_warning_label;

    @FXML
    private AnchorPane login_pane, register_pane;

    @FXML
    private CheckBox remember_user;

    DatabaseManager databaseManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        databaseManager = DatabaseManager.getInstance();
    }

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
        login_warning_label.setText("");
        String email, password;
        email = email_field.getText().trim();
        password = password_field.getText().trim();

        if (email.isEmpty()) {
            email_field.requestFocus();
            login_warning_label.setText("Enter a valid email address.");
            return;
        }

        if (password.isEmpty()) {
            password_field.requestFocus();
            login_warning_label.setText("Enter your password.");
            return;
        }

        /*** verify login credentials in database and return user if succeeded ***/
        User currentUser = databaseManager.loginCredentialsVerified(email, password);
        if (currentUser != null)
            loadUserSession(currentUser);
        else
            login_warning_label.setText("The information you entered did not match any of our records.");
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
            register_warning_label.setText("Enter your first name.");
            return;
        }

        if (LName.isEmpty()) {
            last_name.requestFocus();
            register_warning_label.setText("Enter your last name.");
            return;
        }

        if (mEmail.isEmpty()) {
            email.requestFocus();
            register_warning_label.setText("Enter a valid email address.");
            return;
        }

        if (phone.isEmpty()) {
            phone_number.requestFocus();
            register_warning_label.setText("Enter a valid phone number.");
            return;
        }

        if (mPassword.isEmpty()) {
            password.requestFocus();
            register_warning_label.setText("Enter a a valid password for your account.");
            return;
        }

        if (confirmedPassword.isEmpty()) {
            confirmed_password.requestFocus();
            register_warning_label.setText("Confirm your password.");
            return;
        }

        if (!mPassword.equals(confirmedPassword)) {
            register_warning_label.setText("Passwords do not match.");
            return;
        }

        if (mPassword.length() < 6) {
            password.requestFocus();
            register_warning_label.setText("Password must be at least 6 characters");
            return;
        }

        if (!databaseManager.isEmailTaken(mEmail)) {
            String hash = BCrypt.hashpw(mPassword, BCrypt.gensalt());
            User currentUser = databaseManager.addUser(FName, LName, mEmail, hash, phone);
            if (currentUser != null)
                loadUserSession(currentUser);
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "There was a problem when trying to register.");
                alert.showAndWait();
            }
        } else {
            email.requestFocus();
            Alert alert = new Alert(Alert.AlertType.WARNING, "There is already an account registered with this email address.");
            alert.showAndWait();
        }
    }

    public void loadUserSession(User currentUser) {
        /*** set the current user for this logged in session ***/
        Mediator.getInstance().setCurrentUser(currentUser);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
            login_btn.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



















