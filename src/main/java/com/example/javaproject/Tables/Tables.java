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
			synchronized (tablesSingelton){
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

	public void insertSchueler(ResultSet schuelerSet) throws SQLException {
		tables.computeIfAbsent(TableName.Schueler, k -> new HashMap<>());

		while(schuelerSet.next()){
			Schueler s = createSchueler(schuelerSet);
			tables.get(TableName.Schueler).put(s.getSId(), s);
		}
	}

	public void insertKurs(ResultSet kursSet) throws SQLException {
		tables.computeIfAbsent(TableName.Kurs, k -> new HashMap<>());

		while(kursSet.next()){
			Kurs k = createKurs(kursSet);
			tables.get(TableName.Kurs).put(k.getKId(), k);
		}
	}

	public void insertUnternehmen(ResultSet unternehmenSet) throws SQLException {
		tables.computeIfAbsent(TableName.Unternehmen, k -> new HashMap<>());

		while(unternehmenSet.next()){
			Unternehmen u = createUnternehmen(unternehmenSet);
			tables.get(TableName.Unternehmen).put(u.getUId(), u);
		}
	}

	private Schueler createSchueler(ResultSet set) throws SQLException {
		SimpleIntegerProperty sId = new SimpleIntegerProperty(set.getInt("sId"));
		SimpleIntegerProperty uId = new SimpleIntegerProperty(set.getInt("uId"));
		SimpleIntegerProperty kId = new SimpleIntegerProperty(set.getInt("kId"));
		SimpleStringProperty vorname = new SimpleStringProperty(set.getString("vorname"));
		SimpleStringProperty nachname = new SimpleStringProperty(set.getString("nachname"));
		SimpleStringProperty geschlecht = new SimpleStringProperty(set.getString("geschlecht"));
		SimpleIntegerProperty vorkenntnisse = new SimpleIntegerProperty(set.getInt("vorkenntnisse"));

		return new Schueler(sId, uId, kId, vorname, nachname, geschlecht, vorkenntnisse);
	}

	private Unternehmen createUnternehmen(ResultSet set) throws SQLException {
		SimpleIntegerProperty uId = new SimpleIntegerProperty(set.getInt("uId"));
		SimpleStringProperty name = new SimpleStringProperty(set.getString("name"));

		return new Unternehmen(uId, name);
	}

	private Kurs createKurs(ResultSet set) throws SQLException {
		SimpleIntegerProperty kId = new SimpleIntegerProperty(set.getInt("kId"));
		SimpleStringProperty bezeichnung = new SimpleStringProperty(set.getString("bezeichnung"));
		SimpleStringProperty raum = new SimpleStringProperty(set.getString("raum"));

		return new Kurs(kId, bezeichnung, raum);
	}
}
