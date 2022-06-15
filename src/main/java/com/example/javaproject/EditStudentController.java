package com.example.javaproject;

import com.example.javaproject.Converter.KursConverter;
import com.example.javaproject.Converter.UnternehmenConverter;
import com.example.javaproject.Tables.Kurs;
import com.example.javaproject.Tables.Schueler;
import com.example.javaproject.Tables.Tables;
import com.example.javaproject.Tables.Unternehmen;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditStudentController implements Initializable {
	@FXML
	private ChoiceBox<Kurs> box_kurs;
	@FXML
	private ChoiceBox<Unternehmen> box_unternehmen;
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

	Schueler schueler;
	Schueler changedSchueler;
	boolean changed;
	ArrayList<Kurs> kursArrayList;
	ArrayList<Unternehmen> unternehmenArrayList;
	Tables tables;
	DBConnection dbConnection;

	public EditStudentController(Schueler schueler) {
		this.schueler = schueler;
		this.changedSchueler = new Schueler(schueler);
		this.tables = Tables.getInstance();
		dbConnection = DBConnection.getInstance();
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		text_vorname.setText(schueler.getVorname());
		text_vorname.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.equals(schueler.getVorname())) {
				changed = true;
				changedSchueler.setVorname(newValue);
			} else {
				changed = false;
			}
		});
		text_nachname.setText(schueler.getNachname());
		text_nachname.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.equals(schueler.getNachname())) {
				changed = true;
				changedSchueler.setNachname(newValue);
			} else {
				changed = false;
			}
		});
		text_geschlecht.setText(schueler.getGeschlecht());
		text_geschlecht.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.equals(schueler.getGeschlecht())) {
				changed = true;
				changedSchueler.setGeschlecht(newValue);
			} else {
				changed = false;
			}
		});
		slider_java.setValue(schueler.getVorkenntnisse());
		//only integers
		slider_java.valueProperty().addListener((obs, oldval, newVal) -> {
			slider_java.setValue(newVal.intValue());
			if (!(newVal.intValue() == schueler.getVorkenntnisse())) {
				changed = true;
				changedSchueler.setVorkenntnisse(newVal.intValue());
			} else {
				changed = false;
			}
		});
		label_java.setText(Integer.toString(schueler.getVorkenntnisse()));
		//bind label to value of slider
		label_java.textProperty().bind(Bindings.format("%.2f", slider_java.valueProperty()));
		//fill box_kurs with all kurse and select kurs of student
		this.kursArrayList = tables.getAllKurse();
		for (Kurs kurs : kursArrayList) {
			box_kurs.getItems().add(kurs);
			if (kurs.getKId() == schueler.getKId()) {
				box_kurs.setValue(kurs);
			}
		}
		box_kurs.setConverter(new KursConverter());
		box_kurs.valueProperty().addListener((obs, oldval, newVal) -> {
			changed = (box_kurs.getValue().getKId() != schueler.getKId());
		});
		//fill box_unternehmen with all unternehmen and select unternehmen of student
		this.unternehmenArrayList = tables.getAllUnternehmen();
		for (Unternehmen unternehmen : unternehmenArrayList) {
			box_unternehmen.getItems().add(unternehmen);
			if (unternehmen.getUId() == schueler.getUId()) {
				box_unternehmen.setValue(unternehmen);
			}
		}
		box_unternehmen.setConverter(new UnternehmenConverter());
		box_unternehmen.valueProperty().addListener((obs, oldval, newVal) -> {
			changed = (box_unternehmen.getValue().getUId() != schueler.getUId());
		});
	}

	@FXML
	private void button_delete_click() {
		System.out.println("Exmatrikulieren!");
	}

	@FXML
	private void button_abbrechen_click() {
		Stage stage = (Stage) button_abbrechen.getScene().getWindow();
		stage.close();
	}

	/**
	 * If anything has changed at student information a alert is called with changed information
	 * If Ok method calls updateStudent and updateStudentArrayList in DBConnection with changedStudent
	 */
	@FXML
	private void button_speichern_click() {
		String sUnternehmen = tables.getUnternehmen(schueler.getUId()).getName();
		String sKurs = tables.getKurs(schueler.getKId()).getBezeichnung();

		if (changed) {
			String check = "Sie haben folgende Angaben geändert:\n";
			if (!(schueler.getVorname().equals(changedSchueler.getVorname()))) {
				check = check + "Vorname: " + schueler.getVorname() + " -> " + changedSchueler.getVorname() + "\n";
			}
			if (!(schueler.getNachname().equals(changedSchueler.getNachname()))) {
				check = check + "Nachname: " + schueler.getNachname() + " -> " + changedSchueler.getNachname() + "\n";
			}
			if (!(schueler.getGeschlecht().equals(changedSchueler.getGeschlecht()))) {
				check = check + "Geschlecht: " + schueler.getGeschlecht() + " -> " + changedSchueler.getGeschlecht() + "\n";
			}
			if (!(schueler.getVorkenntnisse() == changedSchueler.getVorkenntnisse())) {
				check = check + "Java-Skills: " + schueler.getVorkenntnisse() + " -> " + changedSchueler.getVorkenntnisse() + "\n";
			}
			if (box_unternehmen.getValue().getUId() != schueler.getUId()) {
				changedSchueler.setUId(box_unternehmen.getValue().getUId());
				check = check + "Unternehmen: " + sUnternehmen + " -> " + box_unternehmen.getValue().getName() + "\n";
			}
			if (box_kurs.getValue().getKId() != schueler.getKId()) {
				changedSchueler.setKId(box_kurs.getValue().getKId());
				check = check + "Kurs: " + sKurs + " -> " + box_kurs.getValue().getBezeichnung() + "\n";
			}
			check = check + "Trotzdem fortfahren?";
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Bestätigung");
			alert.setContentText(check);
			alert.showAndWait().ifPresent(rs -> {
				if (rs == ButtonType.OK) {
					tables.updateSchueler(changedSchueler);
					dbConnection.updateStudent(changedSchueler);
					button_abbrechen_click();
				}
			});
		} else {
			button_abbrechen_click();
		}
	}
}
