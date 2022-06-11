package com.example.javaproject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditStudentController implements Initializable {
    @FXML
    private ChoiceBox<String> box_kurs;
    @FXML
    private ChoiceBox<String> box_unternehmen;
    @FXML
    private Button button_abbrechen;
    @FXML
    private Button button_add_kurs;
    @FXML
    private Button button_add_unternehmen;
    @FXML
    private Button button_delete;
    @FXML
    private Button button_speichern;
    @FXML
    private Label label_java;
    @FXML
    private Slider slider_java;
    @FXML
    private TextField text_geschlecht;
    @FXML
    private TextField text_nachname;
    @FXML
    private TextField text_vorname;

    Student student;
    ArrayList<Kurs> kursArrayList;
    ArrayList<Unternehmen> unternehmenArrayList;
    DBConnection mycon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        text_vorname.setText(student.getVorname());
        text_nachname.setText(student.getNachname());
        text_geschlecht.setText(student.getGeschlecht());
        label_java.setText(Integer.toString(student.getVorkenntnisse()));
        slider_java.setValue(student.getVorkenntnisse());
        this.kursArrayList=mycon.getKursArrayList();
        for (Kurs kurs : kursArrayList){
            box_kurs.getItems().add(kurs.getBezeichnung());
            if(kurs.getkId()==student.getkId()){
                box_kurs.setValue(kurs.getBezeichnung());
            }
        }
        this.unternehmenArrayList=mycon.getUnternehmenArrayList();
        for(Unternehmen unternehmen : unternehmenArrayList){
            box_unternehmen.getItems().add(unternehmen.getName());
            if(unternehmen.getuId()==student.getuId()){
                box_unternehmen.setValue(unternehmen.getName());
            }
        }
    }
    public EditStudentController(Student student,DBConnection mycon){
        this.student=student;
        this.mycon=mycon;
    }

    @FXML
    private void button_delete_click(){
        System.out.println("Exmatrikulieren!");
    }
    @FXML
    private void button_abbrechen_click(){
        Stage stage = (Stage) button_abbrechen.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void button_speichern_click(){
        System.out.println("speichern!");
    }
    @FXML
    private void button_add_kurs_click(){
        System.out.println("neuer Kurs");
    }
    @FXML
    private void button_add_unternehmen_click(){
        System.out.println("neues Unternehmen!");
    }
}
