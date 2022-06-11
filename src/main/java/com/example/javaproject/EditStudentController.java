package com.example.javaproject;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    Student changedStudent;
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
        //only integers
        slider_java.valueProperty().addListener((obs, oldval, newVal) ->
                slider_java.setValue(newVal.intValue()));
        //bind label to value of slider
        label_java.textProperty().bind(Bindings.format("%.2f",slider_java.valueProperty()));
        //fill box_kurs with all kurse and select kurs of student
        this.kursArrayList=mycon.getKursArrayList();
        for (Kurs kurs : kursArrayList){
            box_kurs.getItems().add(kurs.getBezeichnung());
            if(kurs.getkId()==student.getkId()){
                box_kurs.setValue(kurs.getBezeichnung());
            }
        }
        //fill box_unternehmen with all unternehmen and select unternehmen of student
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
        this.changedStudent=student;
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
        String check = "Sie haben folgende Angaben geändert:\n";
        if(!(student.getVorname().equals(text_vorname.getText()))){
            check = check+ "Vorname: "+student.getVorname()+" -> "+text_vorname.getText()+"\n";
        }
        if(!(student.getNachname().equals(text_nachname.getText()))){
            check = check+ "Nachname: "+student.getNachname()+" -> "+text_nachname.getText()+"\n";
        }
        if(!(student.getGeschlecht().equals(text_geschlecht.getText()))){
            check = check+ "Geschlecht: "+student.getGeschlecht()+" -> "+text_geschlecht.getText()+"\n";
        }
        if(!(student.getVorkenntnisse()==slider_java.getValue())){
            check = check+ "Java-Skills: "+student.getVorkenntnisse()+" -> "+slider_java.getValue()+"\n";
        }
        check=check + "Trotzdem fortfahren?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bestätigung");
        alert.setContentText(check);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });
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
