package com.example.javaproject;

import com.example.javaproject.Tables.Kurs;
import com.example.javaproject.Tables.Schueler;
import com.example.javaproject.Tables.Tables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditCourseController implements Initializable {
	@FXML
	private TextField text_kursname;
	@FXML
	private TextField text_kursraum;
	@FXML
	private Button button_abbrechen_kurs;
	@FXML
	private Button button_delete_kurs;
	@FXML
	private Button button_speichern_kurs;
	@FXML
	private Label label_java;
	@FXML
	private Label label_header;

	Kurs kurs;
	Kurs changedkurs;
	boolean changed;
	DBConnection dbConnection;
	Tables tables;

	public EditCourseController(Kurs kurs) {
		this.kurs = kurs;
		this.changedkurs = new Kurs(kurs);
		this.dbConnection = DBConnection.getInstance();
		tables = Tables.getInstance();
		this.changed = false;
	}

	/**
	 * Initializes the EditCourse GUI and enters preexisting attributes into the Text boxes
	 * If a new course is created, its kID is 0. In this case the header label is changed to reflect
	 * this, and the delete course button is left invisible.
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		text_kursraum.setText(this.kurs.getRaum());
		text_kursraum.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.equals(this.kurs.getRaum())) {
				changed = true;
				this.changedkurs.setRaum(newValue);
			} else {
				changed = false;
			}
		});
		text_kursname.setText(this.kurs.getBezeichnung());
		text_kursname.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.equals(this.kurs.getBezeichnung())) {
				changed = true;
				this.changedkurs.setBezeichnung(newValue);
			} else {
				changed = false;
			}
		});
		if (kurs.getKId() == 0) {
			label_header.setText("Neuer Kurs");
			button_delete_kurs.setVisible(false);
		}
	}

	/**
	 * Deletes the currently selected course locally and on database, updates the GUI to reflect this.
	 * If there are students in the course, they will be deleted on confirmation as well.
	 */
	@FXML
	private void button_delete_kurs_click() {
		ArrayList<Schueler> schuelerArrayList = tables.getAllSchueler();
		ArrayList<Schueler> toDeleteList = new ArrayList<Schueler>();
		for(Schueler schueler : schuelerArrayList){
			if(schueler.getKId()== kurs.getKId()){
				toDeleteList.add(schueler);
			}
		}
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Bestätigung");
		if(toDeleteList.isEmpty()){
			alert.setContentText("Wollen Sie den Kurs wirklich unwiderruflich löschen?");
			alert.showAndWait().ifPresent(rs -> {
				if (rs == ButtonType.OK) {
					System.out.println("Kurs wird jetzt gelöscht.");
					dbConnection.deleteKurs(this.kurs);
					tables.removeKurs(kurs.getKId());
					button_abbrechen_kurs_click();
				}
			});
		}else{
			alert.setContentText("Es befinden sich "+toDeleteList.size()+" Studenten in diesem Kurs.\n"
					+"Möchten Sie alle betroffenen Studenten löschen?");
			ButtonType okButton = new ButtonType("Ja", ButtonBar.ButtonData.YES);
			ButtonType noButton = new ButtonType("Nein", ButtonBar.ButtonData.NO);
			ButtonType cancelButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(okButton, noButton, cancelButton);
			alert.showAndWait().ifPresent(type -> {
				if (type == okButton) {
					dbConnection.deleteKurs(kurs);
					tables.removeKurs(kurs.getKId());
					for (Schueler schueler : toDeleteList){
						dbConnection.deleteStudent(schueler);
						tables.removeSchueler(schueler.getSId());
					}
					button_abbrechen_kurs_click();
				} else if (type == noButton) {
					Alert alert1= new Alert(Alert.AlertType.ERROR);
					alert1.setTitle("Fehler");
					alert1.setContentText("Der Kurs kann nicht gelöscht werden.");
					alert1.show();
				} else {
					System.out.println("Abbruch!");
				}
			});
		}
	}

	/**
	 * Closes the stage without applying any changes.
	 */
	@FXML
	private void button_abbrechen_kurs_click() {
		Stage stage = (Stage) button_abbrechen_kurs.getScene().getWindow();
		stage.close();
	}

	/**
	 * If any course information has changed, an alert is called with changed information.
	 * If Ok is pressed, the method calls updateKurs and updateKursArrayList in DBConnection with changedkurs,
	 * applying changes locally and in the database
	 */
	@FXML
	private void button_speichern_click() {
		if(text_kursname.getText().equals("")||text_kursraum.getText().equals("")){
			Alert alert1= new Alert(Alert.AlertType.ERROR);
			alert1.setTitle("Fehler");
			alert1.setContentText("Es fehlen Informationen.");
			alert1.show();
			return;
		}
		if (changed) {
			String check = "Sie haben folgende Angaben geändert:\n";
			if (!(kurs.getBezeichnung().equals(changedkurs.getBezeichnung()))) {
				check = check + "Kursname: " + kurs.getBezeichnung() + " -> " + changedkurs.getBezeichnung() + "\n";
			}
			if (!(kurs.getRaum().equals(changedkurs.getRaum()))) {
				check = check + "Kursraum: " + kurs.getRaum() + " -> " + changedkurs.getRaum() + "\n";
			}

			check = check + "Trotzdem fortfahren?";
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Bestätigung");
			alert.setContentText(check);
			alert.showAndWait().ifPresent(rs -> {
				if (rs == ButtonType.OK) {
					if (kurs.getRaum().equals("") && kurs.getBezeichnung().equals("") && !kurs.getRaum().equals(changedkurs.getRaum()) && !kurs.getBezeichnung().equals(changedkurs.getBezeichnung())) {
						changedkurs.setKId(dbConnection.insertKurs(changedkurs));
					} else if (!kurs.getRaum().equals(changedkurs.getRaum()) && !kurs.getBezeichnung().equals(changedkurs.getBezeichnung())) {
						dbConnection.updateKurs(changedkurs);
					}
					tables.updateKurs(changedkurs);
					button_abbrechen_kurs_click();
				}
			});
		} else {
			button_abbrechen_kurs_click();
		}

	}
}

