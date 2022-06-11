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
    boolean changed;
    ArrayList<Kurs> kursArrayList;
    ArrayList<Unternehmen> unternehmenArrayList;
    DBConnection mycon;

    public EditStudentController(Student student,DBConnection mycon){
        this.student=student;
        this.changedStudent=new Student(student);
        this.mycon=mycon;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        text_vorname.setText(student.getVorname());
        text_vorname.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(student.getVorname())){
                changed=true;
                changedStudent.setVorname(newValue);
            }
        });
        text_nachname.setText(student.getNachname());
        text_nachname.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(student.getNachname())){
                changed=true;
                changedStudent.setNachname(newValue);
            }
        });
        text_geschlecht.setText(student.getGeschlecht());
        text_geschlecht.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(student.getGeschlecht())){
                changed=true;
                changedStudent.setGeschlecht(newValue);
            }
        });
        slider_java.setValue(student.getVorkenntnisse());
        //only integers
        slider_java.valueProperty().addListener((obs, oldval, newVal) -> {
                slider_java.setValue(newVal.intValue());
                if(!(newVal.intValue()==student.getVorkenntnisse())){
                    changed=true;
                    changedStudent.setVorkenntnisse(newVal.intValue());
                    System.out.println("changed");
                }
            });
        label_java.setText(Integer.toString(student.getVorkenntnisse()));
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
        if(changed){
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
            check=check + "Trotzdem fortfahren?";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bestätigung");
            alert.setContentText(check);
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    mycon.updateStudent(changedStudent);
                    mycon.updateStudentArrayList(changedStudent);
                    button_abbrechen_click();
                }
            });
        }else{
            button_abbrechen_click();
        }

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
