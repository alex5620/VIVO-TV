package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML private Pane pane;
    @FXML private Button clientsButton;
    @FXML private Button subButton;
    @FXML private Button packButton;
    @FXML private Button aboutButton;
    @FXML private Button exitButton;

    @FXML
    private void stopApplication()
    {
        Platform.exit();
    }

    @FXML
    private void switchToClientsScene()
    {
        switchTheScene("clients_menu.fxml");
    }

    @FXML
    private void switchToSubScene()
    {
        switchTheScene("sub_menu.fxml");
    }

    @FXML
    private void switchTheScene(String fmxlName)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fmxlName));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 640));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientsButton.setOnMouseEntered(e -> clientsButton.setStyle(Styles.HOVERED_BUTTON_STYLE));
        clientsButton.setOnMouseExited(e -> clientsButton.setStyle(Styles.IDLE_BUTTON_STYLE));
        subButton.setOnMouseEntered(e -> subButton.setStyle(Styles.HOVERED_BUTTON_STYLE));
        subButton.setOnMouseExited(e -> subButton.setStyle(Styles.IDLE_BUTTON_STYLE));
        packButton.setOnMouseEntered(e -> packButton.setStyle(Styles.HOVERED_BUTTON_STYLE));
        packButton.setOnMouseExited(e -> packButton.setStyle(Styles.IDLE_BUTTON_STYLE));
        aboutButton.setOnMouseEntered(e -> aboutButton.setStyle(Styles.HOVERED_BUTTON_STYLE));
        aboutButton.setOnMouseExited(e -> aboutButton.setStyle(Styles.IDLE_BUTTON_STYLE));
        exitButton.setOnMouseEntered(e -> exitButton.setStyle(Styles.HOVERED_BUTTON_STYLE));
        exitButton.setOnMouseExited(e -> exitButton.setStyle(Styles.IDLE_BUTTON_STYLE));
    }
}
