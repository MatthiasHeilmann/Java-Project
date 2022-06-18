package com.example.javaproject;

import com.example.javaproject.Tables.Tables;
import com.example.javaproject.Tables.Kurs;
import com.example.javaproject.Tables.Unternehmen;
import com.example.javaproject.Tables.Schueler;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableArray;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private TableView<Schueler> table_student;
    @FXML
    private TableColumn<Schueler,String> table_student_column_geschlecht;
    @FXML
    private TableColumn<Schueler,Integer> table_student_column_java;
    @FXML
    private TableColumn<Schueler,String> table_student_column_kurs;
    @FXML
    private TableColumn<Schueler,String> table_student_column_nachname;
    @FXML
    private TableColumn<Schueler,String> table_student_column_unternehmen;
    @FXML
    private TableColumn<Schueler, String> table_student_column_vorname;
    @FXML
    private Label table_student_header;
    @FXML
    private Label table_student_header_raum;
    @FXML
    private Button button_show_all;
    @FXML
    private Button button_search;
    @FXML
    private TextField text_search;

    @FXML
    private void button_search_click(){
        if(text_search.isVisible()){
            text_search.setVisible(false);
            table_student_header.setVisible(true);
            button_show_all.setVisible(false);
            fillStudentTable();
        }else {
            text_search.setVisible(true);
            table_student_header.setVisible(false);
            button_show_all.setVisible(true);
            System.out.println("search!");
        }
    }
    @FXML
    private void text_search_typed(){
        String typedText=text_search.getText();
        ArrayList<Schueler> arrayList = tables.getAllSchueler();
        ArrayList<Schueler> newArrayList = new ArrayList<>();
        for(Schueler schueler : arrayList){
            String toFind = schueler.getVorname()+" "+schueler.getNachname();
            if(isSubString(typedText.toLowerCase(),toFind.toLowerCase())){
                newArrayList.add(schueler);
            }
        }
        table_student.getItems().clear();
        insertStudents(newArrayList);
    }

    private boolean isSubString(String s1, String s2){
        boolean val = false;
        int M = s1.length();
        int N = s2.length();
        for (int i = 0; i <= N - M; i++) {
            int j;
            for (j = 0; j < M; j++)
                if (s2.charAt(i + j)!= s1.charAt(j))
                    break;
            if (j == M)
                val = true;
        }
        return val;
    }

    /**
     * Opens a Window where a new student can be added
     */
    @FXML
    public void button_add_student_click(ActionEvent event) throws Exception {
        editStudent(new Schueler(
                new SimpleIntegerProperty(0),
                new SimpleIntegerProperty(0),
                new SimpleIntegerProperty(0),
                new SimpleStringProperty(""),
                new SimpleStringProperty(""),
                new SimpleStringProperty(""),
                new SimpleIntegerProperty(0)
        ));
    }
    /**
     * Opens a Window where a new kurs can be added
     */
    @FXML
    protected void button_add_kurs_click() throws IOException {
        ArrayList<Kurs> kursArrayList = tables.getAllKurse();
        editKurs(new Kurs(new SimpleIntegerProperty(0)
                        , new SimpleStringProperty("")
                        , new SimpleStringProperty("")));
    }
    /**
     * Opens a Window where a new unternehmen can be added
     */
    @FXML
    protected void button_add_unternehmen_click() {
        try {
            editUnternehmen(new Unternehmen(new SimpleIntegerProperty(0),
                                            new SimpleStringProperty("")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Neues Unternehmen!");
    }

    /**
     * calls fillStudentTable
     */
    @FXML
    protected void button_show_all_click(){
        fillStudentTable();
        button_show_all.setVisible(false);
    }

    private DBConnection dbConnection;
    private Tables tables;

    public MainFrameController(){
        dbConnection= DBConnection.getInstance();
        tables = Tables.getInstance();
        tables.addObserver(this);
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
                    TableRow<Schueler> row = new TableRow<>();

                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                            Schueler rowData = row.getItem();
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

        // table_student setup
        table_student_header.setText("Alle Studenten");
        button_show_all.setVisible(false);
        table_student_header_raum.setVisible(false);
        table_student_column_vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        table_student_column_nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        table_student_column_geschlecht.setCellValueFactory(new PropertyValueFactory<>("geschlecht"));
        table_student_column_java.setCellValueFactory(new PropertyValueFactory<>("vorkenntnisse"));
        table_student_column_kurs.setCellValueFactory(studentData -> {
            Kurs sKurs = tables.getKurs(studentData.getValue().getKId());
            return new ReadOnlyStringWrapper(sKurs.getBezeichnung());
        });
        table_student_column_unternehmen.setCellValueFactory(studentData -> {
            Unternehmen sUnternehmen = tables.getUnternehmen(studentData.getValue().getUId());
            return new ReadOnlyStringWrapper(sUnternehmen.getName());
        });

        // table_kurs setup
        table_kurs_column_kurs.setCellValueFactory(new PropertyValueFactory<>("bezeichnung"));

        // table_unternehmen setup
        table_unternehmen_column_unternehmen.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    /**
     * fills student table with all students
     */
    public void fillStudentTable(){
        table_student.getItems().clear();

        ArrayList<Schueler> schuelerArrayList = tables.getAllSchueler();

        for (Schueler schueler : schuelerArrayList){
            table_student.getItems().add(schueler);
        }
            table_student.refresh();
    }

    private void insertStudent(Schueler schueler){
        table_student.getItems().add(schueler);
    }

    private void insertStudents(ArrayList<Schueler> schuelerList){
        table_student.getItems().addAll(schuelerList);
    }

    /**
     * fills student table with all students
     */
    public void fillStudentTableOnKurs(Kurs kurs){
        table_student.getItems().clear();

        ArrayList<Schueler> insertList = new ArrayList<>();
        ArrayList<Schueler> schuelerArrayList = tables.getAllSchueler();

        for (Schueler schueler : schuelerArrayList){
            if(schueler.getKId()==kurs.getKId()){
                insertList.add(schueler);
            }
        }

        insertStudents(insertList);
    }
    public void fillStudentTableOnUnternehmen(Unternehmen unternehmen){
        table_student.getItems().clear();

        ArrayList<Schueler> insertList = new ArrayList<>();
        ArrayList<Schueler> schuelerArrayList = tables.getAllSchueler();

        for (Schueler schueler : schuelerArrayList){
            if(schueler.getUId()==unternehmen.getUId()){
                insertList.add(schueler);
            }
        }

        insertStudents(insertList);
    }

    public void fillKursTable(){
        table_kurs.getItems().clear();
        table_kurs.getItems().addAll(tables.getAllKurse());
    }

    public void fillUnternehmenTable(){
        table_unternehmen.getItems().clear();
        table_unternehmen.getItems().addAll(tables.getAllUnternehmen());
    }

    public void editStudent(Schueler schueler) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("editstudent.fxml"));
        EditStudentController controller = new EditStudentController(schueler);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 470, 350);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("Student bearbeiten");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void editKurs(Kurs kurs) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("editkurs.fxml"));
        EditCourseController controller = new EditCourseController(kurs);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 470, 350);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("Kurs bearbeiten");
        if(kurs.getKId()==0){
            stage.setTitle("Neuer Kurs");
        }
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    private void editUnternehmen(Unternehmen unternehmen) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("editunternehmen.fxml"));
        EditUnternehmenController controller = new EditUnternehmenController(unternehmen);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 470, 350);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("Unternehmen bearbeiten");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void update(Observable o, Object arg) {
        fillStudentTable();
        fillUnternehmenTable();
        fillKursTable();
    }

}