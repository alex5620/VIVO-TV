package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.xml.bind.DatatypeConverter;
import java.beans.EventHandler;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML Pane pane;
    @FXML Button loginButton;
    @FXML TextField username;
    @FXML PasswordField password;
    @FXML Label messageLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setDefaultButton(true);
    }

    @FXML
    public void doLogin()
    {
        if(encryptPassword(password.getText()).equals(LoginDatabaseHandler.getInstance().getHashedPassword(username.getText())))
        {
            FXMLLoader loader = new FXMLLoader(Controller.class.getResource("main_menu.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            double width = 900;
            double height = 640;
            Parent root = loader.getRoot();
            Stage stage = (Stage) pane.getScene().getWindow();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - width) / 2);
            stage.setY((screenBounds.getHeight() - height) / 2);
            stage.setTitle("VIVO TV");
            stage.setScene(new Scene(root, 900, 640));
        }
        else {
            if (!LoginDatabaseHandler.getInstance().checkIfUserExists(username.getText())) {
                messageLabel.setText("Login failed: user doesn't exist.");
            } else {
                messageLabel.setText("Login failed: incorrect password.");
            }
        }
    }

    public String encryptPassword(String password) {
        String myHash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return myHash;
    }
}