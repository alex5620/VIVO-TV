package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("login_form.fxml"));
//        primaryStage.setTitle("Login");
//        primaryStage.setScene(new Scene(root, 400, 200));
//        primaryStage.show();
        Parent root = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
        primaryStage.setTitle("VIVO TV");
        primaryStage.setScene(new Scene(root, 900, 640));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
