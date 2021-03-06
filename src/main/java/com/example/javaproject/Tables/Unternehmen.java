package com.example.javaproject.Tables;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public record Unternehmen(IntegerProperty uId, StringProperty name) implements DataSet {

	public Unternehmen(Unternehmen u) {
		this(new SimpleIntegerProperty(u.getUId())
			, new SimpleStringProperty(u.getName()));
	}

	public int getUId() {
		return uId.get();
	}

	public IntegerProperty uIdProperty() {
		return uId;
	}

	public void setUId(int uId) {
		this.uId.set(uId);
	}

	public String getName() {
		return name.get();
	}

	public StringProperty nameProperty() {
		return name;
	}

	public void setName(String name) {
		this.name.set(name);
	}
}
