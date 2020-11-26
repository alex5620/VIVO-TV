package sample;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMLFileLoader {
    public static void loadFXML(Pane pane, String fxmlName)
    {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(FXMLFileLoader.class.getResource(fxmlName));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 640));
    }
}
