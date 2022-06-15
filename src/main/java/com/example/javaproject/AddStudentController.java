package com.example.javaproject;

import com.example.javaproject.DBConnection;
import com.example.javaproject.Tables.Kurs;
import com.example.javaproject.Tables.Schueler;
import com.example.javaproject.Tables.Tables;
import com.example.javaproject.Tables.Unternehmen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddStudentController implements Initializable{

    @FXML
    private ChoiceBox<String> box_kurs;
    @FXML
    private ChoiceBox<String> box_unternehmen;
    @FXML
    private Button button_abbrechen;
    @FXML
    private Button button_speichern;
    @FXML
    private ToggleGroup geschlecht;
    @FXML
    private Label label_java;
    @FXML
    private RadioButton radio_d;
    @FXML
    private RadioButton radio_m;
    @FXML
    private RadioButton radio_w;
    @FXML
    private Slider slider_java;
    @FXML
    private TextField text_nachname;
    @FXML
    private TextField text_vorname;

    /*Student student;*/
    ArrayList<Kurs> kursArrayList;
    ArrayList<Unternehmen> unternehmenArrayList;
    DBConnection dbConnection;
    Tables table;

    public AddStudentController(){
        this.dbConnection=DBConnection.getInstance();
        table = Tables.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //fill box_kurs with all kurse and select kurs of student
        this.kursArrayList=table.getAllKurse();
        for (Kurs kurs : kursArrayList){
            box_kurs.getItems().add(kurs.getBezeichnung());
            box_kurs.setValue(kurs.getBezeichnung());
        }
        //fill box_unternehmen with all unternehmen and select unternehmen of student
        this.unternehmenArrayList=table.getAllUnternehmen();
        for(Unternehmen unternehmen : unternehmenArrayList){
            box_unternehmen.getItems().add(unternehmen.getName());
            box_unternehmen.setValue(unternehmen.getName());
        }
    }


    @FXML
    void button_abbrechen_click(ActionEvent event) {
        Stage stage = (Stage) button_abbrechen.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void button_speichern_click(){
        /*if(changed){
            String check = "Sie haben folgende Angaben geändert:\n";
            if(!(student.getVorname().equals(changedStudent.getVorname()))){
                check = check+ "Vorname: "+student.getVorname()+" -> "+changedStudent.getVorname()+"\n";
            }
            if(!(student.getNachname().equals(changedStudent.getNachname()))){
                check = check+ "Nachname: "+student.getNachname()+" -> "+changedStudent.getNachname()+"\n";
            }
            if(!(student.getGeschlecht().equals(changedStudent.getGeschlecht()))){
                check = check+ "Geschlecht: "+student.getGeschlecht()+" -> "+changedStudent.getGeschlecht()+"\n";
            }
            if(!(student.getVorkenntnisse()==changedStudent.getVorkenntnisse())){
                check = check+ "Java-Skills: "+student.getVorkenntnisse()+" -> "+changedStudent.getVorkenntnisse()+"\n";
            }
            if(!(student.getUnternehmen().equals(box_unternehmen.getValue()))){
                for(Unternehmen unternehmen : unternehmenArrayList){
                    if(unternehmen.getName().equals(box_unternehmen.getValue())){
                        changedStudent.setUnternehmen(unternehmen.getName());
                        changedStudent.setuId(unternehmen.getuId());
                    }
                }
                check = check + "Unternehmen: " + student.getUnternehmen() + " -> " + changedStudent.getUnternehmen()+"\n";
            }
            if(!(student.getKurs().equals(box_kurs.getValue()))){
                for(Kurs kurs : kursArrayList){
                    if(kurs.getBezeichnung().equals(box_kurs.getValue())){
                        changedStudent.setKurs(kurs.getBezeichnung());
                        changedStudent.setkId(kurs.getkId());
                    }
                }
                check = check + "Kurs: " + student.getKurs() + " -> " + changedStudent.getKurs()+"\n";
            }
            check=check + "Trotzdem fortfahren?";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bestätigung");
            alert.setContentText(check);
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    dbConnection.updateStudent(changedStudent);
                    dbConnection.updateStudentArrayList(changedStudent);
                    button_abbrechen_click();
                }
            });
        }else{
            button_abbrechen_click();
        }*/

    }

}
