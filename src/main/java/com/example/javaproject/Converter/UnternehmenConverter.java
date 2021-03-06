package com.example.javaproject.Converter;

import com.example.javaproject.Tables.Unternehmen;
import javafx.util.StringConverter;

public class UnternehmenConverter extends StringConverter<Unternehmen> {
	@Override
	public String toString(Unternehmen unternehmen) {
		return unternehmen != null? unternehmen.getName() : "N/A";
	}

	@Override
	public Unternehmen fromString(String s) {
		// Not used
		return null;
	}
}
