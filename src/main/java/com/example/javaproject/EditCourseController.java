package com.example.javaproject;

import com.example.javaproject.Tables.Kurs;
import com.example.javaproject.Tables.Tables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
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

	Kurs kurs;
	Kurs changedkurs;
	boolean changed;
	DBConnection dbConnection;
	Tables tables;

	public EditCourseController(Kurs kurs) {
		this.kurs = kurs;
		this.changedkurs = new Kurs(kurs.kId(), kurs.bezeichnung(), kurs.raum());
		this.dbConnection = DBConnection.getInstance();
		tables = Tables.getInstance();
		this.changed = false;
	}

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
	}

	@FXML
	private void button_delete_kurs_click() {
		dbConnection.deleteKurs(kurs);
		tables.removeKurs(kurs.getKId());
		button_abbrechen_kurs_click();
	}

	@FXML
	private void button_abbrechen_kurs_click() {
		Stage stage = (Stage) button_abbrechen_kurs.getScene().getWindow();
		stage.close();
	}

	/**
	 * If anything has changed at course information an alert is called with changed information
	 * If Ok method calls updateStudent and updateStudentArrayList in DBConnection with changedStudent
	 */
	@FXML
	private void button_speichern_click() {
		if (changed) {
			String check = "Sie haben folgende Angaben geändert:\n";
			if (!(kurs.getBezeichnung() == changedkurs.getBezeichnung())) {
				check = check + "Kursname: " + kurs.getBezeichnung() + " -> " + changedkurs.getBezeichnung() + "\n";
			}
			if (!(kurs.getRaum() == changedkurs.getRaum())) {
				check = check + "Kursraum: " + kurs.getRaum() + " -> " + changedkurs.getRaum() + "\n";
			}

			check = check + "Trotzdem fortfahren?";
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Bestätigung");
			alert.setContentText(check);
			alert.showAndWait().ifPresent(rs -> {
				if (rs == ButtonType.OK) {
					if (!kurs.getRaum().equals("") && !kurs.getBezeichnung().equals("")) {
						dbConnection.updateKurs(changedkurs);
					} else if (!kurs.getRaum().equals(changedkurs.getRaum()) && !kurs.getBezeichnung().equals(changedkurs.getBezeichnung())) {
						dbConnection.insertKurs(changedkurs);
					}
					tables.updateKurs(kurs.getKId(), changedkurs);
					button_abbrechen_kurs_click();
				}
			});
		} else {
			button_abbrechen_kurs_click();
		}

	}
}
