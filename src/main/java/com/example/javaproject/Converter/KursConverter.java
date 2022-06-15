package com.example.javaproject.Converter;

import com.example.javaproject.Tables.Kurs;
import javafx.util.StringConverter;

public class KursConverter extends StringConverter<Kurs> {
	@Override
	public String toString(Kurs kurs) {
		return kurs.getBezeichnung();
	}

	@Override
	public Kurs fromString(String s) {
		// Not used
		return null;
	}
}
