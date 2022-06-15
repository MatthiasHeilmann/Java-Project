package com.example.javaproject;

import com.example.javaproject.Tables.Schueler;
import com.example.javaproject.Tables.Tables;
import com.example.javaproject.Tables.Unternehmen;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Observer;
import java.util.ResourceBundle;

public class EditUnternehmenController implements Initializable {

    @FXML
    private Button button_abbrechen;
    @FXML
    private Button button_delete;
    @FXML
    private Button button_speichern;
    @FXML
    private Label label_header;
    @FXML
    private TextField text_name;

    Unternehmen unternehmen;
    DBConnection dbConnection;
    Tables tables;

    public EditUnternehmenController(Unternehmen unternehmen){
        this.unternehmen = new Unternehmen(unternehmen);
        this.dbConnection=DBConnection.getInstance();
        this.tables = Tables.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        text_name.setText(unternehmen.getName());
    }

    @FXML
    private void button_abbrechen_click() {
        Stage stage = (Stage) button_abbrechen.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void button_delete_click() {
        ArrayList<Schueler> schuelerArrayList = tables.getAllSchueler();
        ArrayList<Schueler> toDeleteList = new ArrayList<Schueler>();
        for(Schueler schueler : schuelerArrayList){
            if(schueler.getUId()== unternehmen.getUId()){
                toDeleteList.add(schueler);
            }
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bestätigung");
        if(toDeleteList.isEmpty()){
            alert.setContentText("Wollen Sie wirklich das Unternehmen unwiderruflich löschen?");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Unternehmen muss jetzt gelöscht werden.");
                    dbConnection.deleteUnternehmen(this.unternehmen);
                    tables.removeUnternehmen(unternehmen.getUId());
                    button_abbrechen_click();
                }
            });
        }else{
            alert.setContentText("Es befinden sich "+toDeleteList.size()+" Studenten in diesem Unternehmen.\n"
                    +"Möchten Sie alle betroffenen Studenten löschen?");
            ButtonType okButton = new ButtonType("Ja", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("Nein", ButtonBar.ButtonData.NO);
            ButtonType cancelButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(okButton, noButton, cancelButton);
            alert.showAndWait().ifPresent(type -> {
                if (type == okButton) {
                    dbConnection.deleteUnternehmen(this.unternehmen);
                    tables.removeUnternehmen(unternehmen.getUId());
                    for (Schueler schueler : toDeleteList){
                        dbConnection.deleteStudent(schueler);
                        tables.removeSchueler(schueler.getSId());
                    }
                    button_abbrechen_click();
                } else if (type == noButton) {
                    Alert alert1= new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Fehler");
                    alert1.setContentText("Das Unternehmen kann nicht gelöscht werden.");
                    alert1.show();
                } else {
                    System.out.println("Abbruch!");
                }
            });
        }

    }

    @FXML
    private void button_speichern_click() {
        if (!(text_name.getText().equals(unternehmen.getName()))){
            String check = "Sie haben folgende Angaben geändert:\n"+
                    "Name: "+unternehmen.getName()+" -> "+text_name.getText()+
                    "\nTrotzdem fortfahren?";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bestätigung");
            alert.setContentText(check);
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    unternehmen.setName(text_name.getText());
                    dbConnection.updateUnternehmen(unternehmen);
                    tables.updateUnternehmen(unternehmen);
                    button_abbrechen_click();
                }
            });
        }
    }

}
