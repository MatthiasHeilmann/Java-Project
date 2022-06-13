package com.example.javaproject.Tables;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public record Kurs(IntegerProperty kId, StringProperty bezeichnung, StringProperty raum) implements DataSet {
	public int getKId() {
		return kId.get();
	}

	public IntegerProperty kIdProperty() {
		return kId;
	}

	public void setKId(int kId) {
		this.kId.set(kId);
	}

	public String getBezeichnung() {
		return bezeichnung.get();
	}

	public StringProperty bezeichnungProperty() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung.set(bezeichnung);
	}

	public String getRaum() {
		return raum.get();
	}

	public StringProperty raumProperty() {
		return raum;
	}

	public void setRaum(String raum) {
		this.raum.set(raum);
	}
}
