package com.example.javaproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class of project
 */

public class Main extends Application {


DBConnection dbConnection = new DBConnection();
//    MainFrameController mainFrameController = new MainFrameController();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainframe.fxml"));
        fxmlLoader.setController(new MainFrameController(dbConnection));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("mainframe.css").toExternalForm());
        stage.setTitle("DHBW Datenbank");
        stage.setScene(scene);
        stage.show();
    }

    public Main(){

    }

    public static void main(String[] args) {
        launch();
    }
}