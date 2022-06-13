package com.example.javaproject.Tables;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Tables {
	enum TableName{Schueler, Kurs, Unternehmen}
	private final HashMap<TableName, HashMap<Integer, DataSet>> tables;
	private static volatile Tables tablesSingelton = null;

	public static Tables getInstance(){
		if(tablesSingelton == null){
			synchronized (Tables.class){
				if(tablesSingelton == null){
					tablesSingelton = new Tables();
				}
			}
		}
		return tablesSingelton;
	}

	private Tables() {
		this.tables = new HashMap<>();
	}

	public Schueler getSchueler(int sId){
		return (Schueler) tables.get(TableName.Schueler).get(sId);
	}

	public Unternehmen getUnternehmen(int uId){
		return (Unternehmen) tables.get(TableName.Unternehmen).get(uId);
	}

	public Kurs getKurs(int kId){
		return (Kurs) tables.get(TableName.Kurs).get(kId);
	}

	public ArrayList<Schueler> getAllSchueler(){
		ArrayList<Schueler> retList = new ArrayList<Schueler>();
		tables.get(TableName.Schueler).values().forEach(v -> {
			retList.add((Schueler)v);
		});
		return retList;
	}

	public ArrayList<Unternehmen> getAllUnternehmen(){
		ArrayList<Unternehmen> retList = new ArrayList<Unternehmen>();
		tables.get(TableName.Unternehmen).values().forEach(v -> {
			retList.add((Unternehmen) v);
		});
		return retList;
	}

	public ArrayList<Kurs> getAllKurse(){
		ArrayList<Kurs> retList = new ArrayList<Kurs>();
		tables.get(TableName.Kurs).values().forEach(v -> {
			retList.add((Kurs)v);
		});
		return retList;
	}

	public void updateSchueler(int id, Schueler s){
		// TODO maybe unsafe
		tables.get(TableName.Schueler).put(id, s);
	}

	public void updateKurs(int id, Kurs k){
		tables.get(TableName.Kurs).put(id, k);
	}

	public void updateUnternehmen(int id, Unternehmen u){
		tables.get(TableName.Unternehmen).put(id, u);
	}

	public void removeSchueler(int id){
		tables.get(TableName.Schueler).remove(id);
	}

	public void removeKurs(int id){
		tables.get(TableName.Kurs).remove(id);
	}

	public void removeUnternehmen(int id){
		tables.get(TableName.Unternehmen).remove(id);
	}

	public void insertSchueler(ArrayList<String> schuelerSet) throws SQLException {
		tables.computeIfAbsent(TableName.Schueler, k -> new HashMap<>());

		for(String schueler: schuelerSet){
			Schueler s = createSchueler(schueler);
			tables.get(TableName.Schueler).put(s.getSId(), s);
		}
	}

	public void insertKurs(ArrayList<String> kursSet) throws SQLException {
		tables.computeIfAbsent(TableName.Kurs, k -> new HashMap<>());

		for(String kurs : kursSet){
			Kurs k = createKurs(kurs);
			tables.get(TableName.Kurs).put(k.getKId(), k);
		};
	}

	public void insertUnternehmen(ArrayList<String> unternehmenSet) throws SQLException {
		tables.computeIfAbsent(TableName.Unternehmen, k -> new HashMap<>());

		for(String unternehmen : unternehmenSet){
			Unternehmen u = createUnternehmen(unternehmen);
			tables.get(TableName.Unternehmen).put(u.getUId(), u);
		}
	}

	private Schueler createSchueler(String schuelerSet) throws SQLException {
		String[] values = schuelerSet.split(",");

		SimpleIntegerProperty sId = new SimpleIntegerProperty(Integer.parseInt(values[0]));
		SimpleIntegerProperty uId = new SimpleIntegerProperty(Integer.parseInt(values[1]));
		SimpleIntegerProperty kId = new SimpleIntegerProperty(Integer.parseInt(values[2]));
		SimpleStringProperty vorname = new SimpleStringProperty(values[3]);
		SimpleStringProperty nachname = new SimpleStringProperty(values[4]);
		SimpleStringProperty geschlecht = new SimpleStringProperty(values[5]);
		SimpleIntegerProperty vorkenntnisse = new SimpleIntegerProperty(Integer.parseInt(values[6]));

		return new Schueler(sId, uId, kId, vorname, nachname, geschlecht, vorkenntnisse);
	}

	private Unternehmen createUnternehmen(String unternehmenSet) throws SQLException {
		String[] values = unternehmenSet.split(",");
		SimpleIntegerProperty uId = new SimpleIntegerProperty(Integer.parseInt(values[0]));
		SimpleStringProperty name = new SimpleStringProperty(values[1]);

		return new Unternehmen(uId, name);
	}

	private Kurs createKurs(String kursSet) throws SQLException {
		String[] values = kursSet.split(",");

		SimpleIntegerProperty kId = new SimpleIntegerProperty(Integer.parseInt(values[0]));
		SimpleStringProperty bezeichnung = new SimpleStringProperty(values[1]);
		SimpleStringProperty raum = new SimpleStringProperty(values[2]);

		return new Kurs(kId, bezeichnung, raum);
	}
}
