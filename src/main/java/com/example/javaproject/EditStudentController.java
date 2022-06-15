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
	private RadioButton radio_d;
	@FXML
	private RadioButton radio_m;
	@FXML
	private RadioButton radio_w;
	@FXML
	private TextField text_nachname;
	@FXML
	private TextField text_vorname;
	@FXML
	private Label label_header;

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
		if(schueler.getGeschlecht().equals("m")){
			radio_m.setSelected(true);
		}else if(schueler.getGeschlecht().equals("w")){
			radio_w.setSelected(true);
		}else if(schueler.getGeschlecht().equals("d")){
			radio_d.setSelected(true);
		}
		radio_m.selectedProperty().addListener((obs, oldval, newVal) -> {
			if(radio_m.isSelected()){
				if(!(schueler.getGeschlecht().equals("m"))){
					changedSchueler.setGeschlecht("m");
					changed=true;
				}else{
					changed=false;
					changedSchueler.setGeschlecht(schueler.getGeschlecht());
				}
			}
		});
		radio_w.selectedProperty().addListener((obs, oldval, newVal) -> {
			if(radio_w.isSelected()){
				if(!(schueler.getGeschlecht().equals("w"))){
					changedSchueler.setGeschlecht("w");
					changed=true;
				}else{
					changed=false;
					changedSchueler.setGeschlecht(schueler.getGeschlecht());
				}
			}
		});
		radio_d.selectedProperty().addListener((obs, oldval, newVal) -> {
			if(radio_d.isSelected()){
				if(!(schueler.getGeschlecht().equals("d"))){
					changedSchueler.setGeschlecht("d");
					changed=true;
				}else{
					changed=false;
					changedSchueler.setGeschlecht(schueler.getGeschlecht());
				}
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
		// TODO @Sean Converter funktioniert nicht, wenn schueler keinen validen Kurs/Unternehmen hat!
		// TODO Boxen sind deswegen noch leer
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
			changedSchueler.setKId(box_kurs.getValue().getKId());
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
			changedSchueler.setUId(box_unternehmen.getValue().getUId());
			changed = (box_unternehmen.getValue().getUId() != schueler.getUId());
		});
		if(schueler.getSId()==0){
			label_header.setText("Neuer Student");
			button_delete.setVisible(false);
		}

	}

	@FXML
	private void button_delete_click() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Bestätigung");
		alert.setContentText("Sind Sie sicher, dass sie den Studenten " +schueler.getVorname()+" "
				+schueler.getNachname()+ " exmatrikulieren wollen?");
		alert.showAndWait().ifPresent(rs -> {
			if (rs == ButtonType.OK) {
				System.out.println("Student wird exmatrikuliert...");
				dbConnection.deleteStudent(schueler);
				tables.removeSchueler(schueler.getSId());
				button_abbrechen_click();
			}
		});
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
		if(schueler.getSId()!=0) {
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
		}else{
			if(text_vorname.getText().equals("")
					||text_nachname.getText().equals("")
					||box_unternehmen.getValue()==null
					||box_kurs==null
					||!(radio_d.isSelected()||radio_m.isSelected()||radio_w.isSelected())){
				Alert alert1= new Alert(Alert.AlertType.ERROR);
				alert1.setTitle("Fehler");
				alert1.setContentText("Es fehlen Informationen.");
				alert1.show();
			}else{
			   	changedSchueler.setSId(dbConnection.insertStudent(changedSchueler));
				tables.updateSchueler(changedSchueler);
				button_abbrechen_click();
			}
		}
	}
}
