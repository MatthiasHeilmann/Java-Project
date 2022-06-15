package com.example.javaproject.Tables;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public record Schueler(IntegerProperty sId, IntegerProperty uId, IntegerProperty kId, StringProperty vorname,
                       StringProperty nachname, StringProperty geschlecht,
                       IntegerProperty vorkenntnisse) implements DataSet {


	/**
	 * For creating a "deep" copy of the given schueler
	 * @param s schueler to be copied
	 */
	public Schueler(Schueler s){
		this(new SimpleIntegerProperty(s.getSId()), new SimpleIntegerProperty(s.getUId())
				, new SimpleIntegerProperty(s.getKId()), new SimpleStringProperty(s.getVorname())
				, new SimpleStringProperty(s.getNachname()), new SimpleStringProperty(s.getGeschlecht())
				, new SimpleIntegerProperty(s.getVorkenntnisse()));
	}

	public IntegerProperty sIdProperty() {
		return sId;
	}

	public int getSId() {
		return sId.get();
	}

	public int getUId() {
		return uId.get();
	}

	public IntegerProperty uIdProperty() {
		return uId;
	}

	public int getKId() {
		return kId.get();
	}

	public IntegerProperty kIdProperty() {
		return kId;
	}

	public String getVorname() {
		return vorname.get();
	}

	public StringProperty vornameProperty() {
		return vorname;
	}

	public String getNachname() {
		return nachname.get();
	}

	public StringProperty nachnameProperty() {
		return nachname;
	}

	public StringProperty geschlechtProperty() {
		SimpleStringProperty retStr = new SimpleStringProperty();
		if (geschlecht.get().charAt(0) == 'm') retStr.set("Männlich");
		else if (geschlecht.get().charAt(0) == 'w') retStr.set("Weiblich");
		else retStr.set("Divers");
		return retStr;
	}

	public String getGeschlecht() {
		return geschlecht.get();
	}

	public int getVorkenntnisse() {
		return vorkenntnisse.get();
	}

	public IntegerProperty vorkenntnisseProperty() {
		return vorkenntnisse;
	}

	public void setSId(int sId) {
		this.sId.set(sId);
	}

	public void setUId(int uId) {
		this.uId.set(uId);
	}

	public void setKId(int kId) {
		this.kId.set(kId);
	}

	public void setVorname(String vorname) {
		this.vorname.set(vorname);
	}

	public void setNachname(String nachname) {
		this.nachname.set(nachname);
	}

	public void setGeschlecht(String geschlecht) {
		this.geschlecht.set(geschlecht);
	}

	public void setVorkenntnisse(int vorkenntnisse) {
		this.vorkenntnisse.set(vorkenntnisse);
	}
}
