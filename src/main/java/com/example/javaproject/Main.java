package com.example.javaproject;

import com.example.javaproject.Tables.Tables;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Main class of project
 */

public class Main extends Application {


//    MainFrameController mainFrameController = new MainFrameController();

    DBConnection dbc;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            initializeData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainframe.fxml"));
        fxmlLoader.setController(new MainFrameController());
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("mainframe.css").toExternalForm());
        stage.setTitle("DHBW Datenbank");
        stage.setOnCloseRequest(e -> {
            stage.close();
            e.consume();
        });
        stage.setScene(scene);
        stage.show();
    }

    private void initializeData() throws SQLException {
        dbc = DBConnection.getInstance();
        Tables tables = Tables.getInstance();

        tables.insertKurs(dbc.getAllKurs());
        tables.insertSchueler(dbc.getAllSchueler());
        tables.insertUnternehmen(dbc.getAllUnternehmen());
    }

    public Main(){

    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            DBConnection.getInstance().closeConnection();
            System.out.println("Closing the Connection");
        }));
        launch();
    }
}