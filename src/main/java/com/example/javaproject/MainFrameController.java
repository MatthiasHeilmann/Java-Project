package com.example.javaproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainFrameController implements Initializable {
    @FXML
    private Label label_headline;
    @FXML
    private TableView<Kurs> table_kurs;
    @FXML
    private TableColumn<Kurs, String> table_kurs_column_kurs;
    @FXML
    private TableView<Student> table_student;
    @FXML
    private TableColumn<Student,String> table_student_column_geschlecht;
    @FXML
    private TableColumn<Student,Integer> table_student_column_java;
    @FXML
    private TableColumn<Student,String> table_student_column_kurs;
    @FXML
    private TableColumn<Student,String> table_student_column_nachname;
    @FXML
    private TableColumn<Student,String> table_student_column_unternehmen;
    @FXML
    private TableColumn<Student, String> table_student_column_vorname;
    @FXML
    private Label table_student_header;
    @FXML
    private Button button_show_all;

    /**
     * Opens a Window where a new student can be added
     */
    @FXML
    protected void button_add_student_click() {
        System.out.println("Neuer Student!");
    }
    /**
     * Opens a Window where a new kurs can be added
     */
    @FXML
    protected void button_add_kurs_click() {
        System.out.println("Neuer Kurs!");
    }
    /**
     * Opens a Window where a new unternehmen can be added
     */
    @FXML
    protected void button_add_unternehmen_click() {
        System.out.println("Neues Unternehmen!");
    }

    /**
     * calls fillStudentTable
     */
    @FXML
    protected void button_show_all_click(){
        fillStudentTable();
    }

    private DBConnection mycon = new DBConnection();
    private ArrayList<Student> studentArrayList = mycon.getAllSchueler();
    private ArrayList<Kurs> kursArrayList = mycon.getAllKurs();
    private ArrayList<Unternehmen> unternehmenArrayList = mycon.getAllUnternehmen();

    /**
     * Initialize method gets called when mainframe is launched
     *
     * fills table_kurs and table_students
     *
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillKursTable();
        fillStudentTable();
        table_kurs.setRowFactory(tv -> {
                    TableRow<Kurs> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                            Kurs rowData = row.getItem();
                            table_student_header.setText("Kurs: "+rowData.getBezeichnung());
                            fillStudentTableOnKurs(rowData);
                            button_show_all.setVisible(true);
                            System.out.println(rowData.getBezeichnung()+" clicked");
                        }
                    });
                    return row ;
        });
        table_student.setRowFactory(tv -> {
                    TableRow<Student> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                            Student rowData = row.getItem();
                            System.out.println(rowData.getVorname()+" "+rowData.getNachname()+" clicked");
                        }
                    });
                    return row ;
        });
    }


    /**
     * fills student table with all students
     */
    public void fillStudentTable(){
        table_student.getItems().clear();
        table_student_header.setText("Alle Kurse");
        button_show_all.setVisible(false);
        table_student_column_vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        table_student_column_nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        table_student_column_geschlecht.setCellValueFactory(new PropertyValueFactory<>("geschlecht"));
        table_student_column_java.setCellValueFactory(new PropertyValueFactory<>("vorkenntnisse"));
        table_student_column_kurs.setCellValueFactory(new PropertyValueFactory<>("kurs"));
        table_student_column_unternehmen.setCellValueFactory(new PropertyValueFactory<>("unternehmen"));

        for (Student student : studentArrayList){
            for(Kurs kurs : kursArrayList) {
                if (kurs.getkId() == student.getkId()) {
                    student.setKurs(kurs.getBezeichnung());
                }
            }
            for(Unternehmen unternehmen : unternehmenArrayList){
                if(unternehmen.getuId()==student.getuId()){
                    student.setUnternehmen(unternehmen.getName());
                }
            }
            table_student.getItems().add(student);
        }
    }
    /**
     * fills student table with all students
     */
    public void fillStudentTableOnKurs(Kurs kurs){
        table_student.getItems().clear();
        table_student_column_vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        table_student_column_nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        table_student_column_geschlecht.setCellValueFactory(new PropertyValueFactory<>("geschlecht"));
        table_student_column_java.setCellValueFactory(new PropertyValueFactory<>("vorkenntnisse"));
        table_student_column_kurs.setCellValueFactory(new PropertyValueFactory<>("kurs"));
        table_student_column_unternehmen.setCellValueFactory(new PropertyValueFactory<>("unternehmen"));

        for (Student student : studentArrayList){
            if(student.getkId()==kurs.getkId()){
                student.setKurs(kurs.getBezeichnung());
                for(Unternehmen unternehmen : unternehmenArrayList){
                    if(unternehmen.getuId()==student.getuId()){
                        student.setUnternehmen(unternehmen.getName());
                    }
                }
                table_student.getItems().add(student);
            }
        }
    }

    /**
     * fills kurs table with alle kurse in database
     */
    public void fillKursTable(){
        ObservableList<Kurs> observableList = FXCollections.observableArrayList(new DBConnection().getAllKurs());
        table_kurs_column_kurs.setCellValueFactory(new PropertyValueFactory<>("bezeichnung"));
        for (Kurs k : observableList){
            table_kurs.getItems().add(k);
        }
    }

    public MainFrameController(){
//        fillKursTable();
    }
}