package com.example.javaproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MainFrameController implements Initializable, Observer {
    @FXML
    private Label label_headline;
    @FXML
    private TableView<Kurs> table_kurs;
    @FXML
    private TableColumn<Kurs, String> table_kurs_column_kurs;
    @FXML
    private TableView<Unternehmen> table_unternehmen;
    @FXML
    private TableColumn<Unternehmen,String> table_unternehmen_column_unternehmen;
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
    private Label table_student_header_raum;
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
    protected void button_add_kurs_click() throws IOException {
        ArrayList<Kurs> kursArrayList = dbConnection.getKursArrayList();
        int max=0;
        for (Kurs kurs:
             kursArrayList) {
            if(kurs.getkId()>max){
                max=kurs.getkId();
            }
        }
        editKurs(new Kurs(max+1,"",""));
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

    private DBConnection dbConnection;

    public MainFrameController(DBConnection dbConnection){
        this.dbConnection=dbConnection;
        dbConnection.addObserver(this);
    }

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
        fillUnternehmenTable();
        fillStudentTable();
        table_kurs.setRowFactory(tv -> {
            TableRow<Kurs> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    Kurs rowData = row.getItem();
                    table_student_header.setText(rowData.getBezeichnung());
                    fillStudentTableOnKurs(rowData);
                    button_show_all.setVisible(true);
                    table_student_header_raum.setText("| Raum: "+rowData.getRaum());
                    table_student_header_raum.setVisible(true);
                    System.out.println(rowData.getBezeichnung()+" clicked");
                }else if(event.getClickCount() == 2 && (!row.isEmpty())){
                    Kurs rowData = row.getItem();
                    try{
                        editKurs(rowData);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            return row ;
        });
        table_unternehmen.setRowFactory(tv -> {
            TableRow<Unternehmen> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    Unternehmen rowData = row.getItem();
                    table_student_header.setText(rowData.getName());
                    fillStudentTableOnUnternehmen(rowData);
                    button_show_all.setVisible(true);
                    table_student_header_raum.setVisible(false);
                    System.out.println(rowData.getName()+" clicked");
                }else if(event.getClickCount() == 2 && (!row.isEmpty())){
                    Unternehmen rowData = row.getItem();
                    try{
                        editUnternehmen(rowData);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
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
                    try {
                    editStudent(rowData);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
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
        table_student_header.setText("Alle Studenten");
        button_show_all.setVisible(false);
        table_student_header_raum.setVisible(false);
        table_student_column_vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        table_student_column_nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        table_student_column_geschlecht.setCellValueFactory(new PropertyValueFactory<>("geschlecht"));
        table_student_column_java.setCellValueFactory(new PropertyValueFactory<>("vorkenntnisse"));
        table_student_column_kurs.setCellValueFactory(new PropertyValueFactory<>("kurs"));
        table_student_column_unternehmen.setCellValueFactory(new PropertyValueFactory<>("unternehmen"));

        ArrayList<Student> studentArrayList = dbConnection.getStudentArrayList();
        ArrayList<Unternehmen> unternehmenArrayList = dbConnection.getUnternehmenArrayList();
        ArrayList<Kurs> kursArrayList = dbConnection.getKursArrayList();

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
            table_student.refresh();
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

        ArrayList<Student> studentArrayList = dbConnection.getStudentArrayList();
        ArrayList<Unternehmen> unternehmenArrayList = dbConnection.getUnternehmenArrayList();

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
    public void fillStudentTableOnUnternehmen(Unternehmen unternehmen){
        table_student.getItems().clear();
        table_student_column_vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        table_student_column_nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        table_student_column_geschlecht.setCellValueFactory(new PropertyValueFactory<>("geschlecht"));
        table_student_column_java.setCellValueFactory(new PropertyValueFactory<>("vorkenntnisse"));
        table_student_column_kurs.setCellValueFactory(new PropertyValueFactory<>("kurs"));
        table_student_column_unternehmen.setCellValueFactory(new PropertyValueFactory<>("unternehmen"));

        ArrayList<Student> studentArrayList = dbConnection.getStudentArrayList();
        ArrayList<Kurs> kursArrayList = dbConnection.getKursArrayList();

        for (Student student : studentArrayList){
            if(student.getuId()==unternehmen.getuId()){
                student.setUnternehmen(unternehmen.getName());
                for(Kurs kurs : kursArrayList){
                    if(kurs.getkId()==student.getkId()){
                        student.setKurs(kurs.getBezeichnung());
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
        table_kurs.getItems().clear();
        ObservableList<Kurs> observableList = FXCollections.observableArrayList(dbConnection.getKursArrayList());
        table_kurs_column_kurs.setCellValueFactory(new PropertyValueFactory<>("bezeichnung"));
        for (Kurs k : observableList){
            table_kurs.getItems().add(k);
        }
    }

    public void fillUnternehmenTable(){
        table_unternehmen.getItems().clear();
        ObservableList<Unternehmen> observableList = FXCollections.observableArrayList(dbConnection.getUnternehmenArrayList());
        table_unternehmen_column_unternehmen.setCellValueFactory(new PropertyValueFactory<>("name"));
        for (Unternehmen u : observableList){
            table_unternehmen.getItems().add(u);
        }
    }

    public void editStudent(Student student) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("editstudent.fxml"));
        EditStudentController controller = new EditStudentController(student,dbConnection);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 470, 350);
        scene.getStylesheets().add(getClass().getResource("editstudent.css").toExternalForm());
        stage.setTitle("Student bearbeiten");
        stage.setScene(scene);
        stage.show();
    }

    public void editKurs(Kurs kurs) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("editkurs.fxml"));
        EditCourseController controller = new EditCourseController(kurs,dbConnection);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 470, 350);
        scene.getStylesheets().add(getClass().getResource("editkurs.css").toExternalForm());
        stage.setTitle("Kurs bearbeiten");
        stage.setScene(scene);
        stage.show();
    }


    private void editUnternehmen(Unternehmen unternehmen) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("editunternehmen.fxml"));
        EditUnternehmenController controller = new EditUnternehmenController(unternehmen,dbConnection);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 470, 350);
        scene.getStylesheets().add(getClass().getResource("editunternehmen.css").toExternalForm());
        stage.setTitle("Unternehmen bearbeiten");
        stage.setScene(scene);
        stage.show();
    }

    public MainFrameController(){
//        fillKursTable();
    }

    @Override
    public void update(Observable o, Object arg) {
        fillStudentTable();
        fillUnternehmenTable();
        fillKursTable();
    }
}