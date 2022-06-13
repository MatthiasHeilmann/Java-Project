package com.example.javaproject;

import com.example.javaproject.Tables.Schueler;
import com.example.javaproject.Tables.Kurs;
import com.example.javaproject.Tables.Unternehmen;

import java.sql.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Model class of project
 * DBConnection class creates a connection to database and communicates with it
 */
public class DBConnection extends Observable {
    private Connection connection;
    private final String connectionUrl = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11495010";
    private final String userName = "sql11495010";
    private final String password = "SRwSuf13UR";

    private static volatile DBConnection dbcSingelton = null;

    public static DBConnection getInstance(){
        if(dbcSingelton == null){
            synchronized (DBConnection.class){
                if(dbcSingelton == null){
                    dbcSingelton = new DBConnection();
                }
            }
        }
        return dbcSingelton;
    }

    private DBConnection(){
        createConnection();
    }

    private void createConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(this.connectionUrl,this.userName,this.password);
            System.out.println("New Connection created.");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * gets all Students ordered by "vorname" from database and returns them in a List of Strings
     * separating the attributes by ","
     *
     * @return ArrayList<Student>
     */
    public ArrayList<String> getAllSchueler(){
        ArrayList<String> arrayList = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM schueler ORDER BY vorname;");
            while (resultSet.next()){
                arrayList.add(""
                        + resultSet.getInt("sId") + ","
                        + resultSet.getInt("uId") + ","
                        + resultSet.getInt("kId") + ","
                        + resultSet.getString("vorname") + ","
                        + resultSet.getString("nachname") + ","
                        + resultSet.getString("geschlecht") + ","
                        + resultSet.getInt("vorkenntnisse")
                );
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return arrayList;
    }

    /**
     * gets all Kurs from database and returns them in a List of Strings
     * separating the attributes by ","
     *
     * @return ArrayList<Kurs>
     */
    public ArrayList<String> getAllKurs(){
        ArrayList<String> arrayList = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM kurs;");
            while (resultSet.next()){
                arrayList.add(""
                        + resultSet.getInt("kId") + ","
                        + resultSet.getString("bezeichnung")+ ","
                        + resultSet.getString("raum")
                );
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return arrayList;
    }

    /**
     * gets all Unternehmen from database and returns them in a List of Strings
     * separating the attributes by ","
     *
     * @return ArrayList<Unternehmen>
     */
    public ArrayList<String> getAllUnternehmen(){
        ArrayList<String> arrayList = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM unternehmen;");
            while (resultSet.next()){
                arrayList.add(""
                        + resultSet.getInt("uId") + ","
                        + resultSet.getString("name")
                );
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return arrayList;
    }

    /**
     * inserts a Student to database using prepared statement
     *
     * @param schueler
     */
    public void insertStudent(Schueler schueler){
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO schueler (uId,kId,nachname,vorname,geschlecht,vorkenntnisse) "+
                            "Values(?,?,?,?,?,?);");
            preparedStatement.setInt(1, schueler.getUId());
            preparedStatement.setInt(2, schueler.getKId());
            preparedStatement.setString(3, schueler.getNachname());
            preparedStatement.setString(4, schueler.getVorname());
            preparedStatement.setString(5, schueler.getGeschlecht());
            preparedStatement.setInt(6, schueler.getVorkenntnisse());
            preparedStatement.execute();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     * updates a student in database
     *
     * @param schueler
     */
    public void updateStudent(Schueler schueler){
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "UPDATE schueler SET uId = ?,kId = ?,nachname = ?,vorname = ?,geschlecht = ?,vorkenntnisse = ? "+
                            "WHERE sId = "+ schueler.getSId()+";"
            );
            preparedStatement.setInt(1, schueler.getUId());
            preparedStatement.setInt(2, schueler.getKId());
            preparedStatement.setString(3, schueler.getNachname());
            preparedStatement.setString(4, schueler.getVorname());
            preparedStatement.setString(5, schueler.getGeschlecht());
            preparedStatement.setInt(6, schueler.getVorkenntnisse());
            preparedStatement.execute();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     * inserts new kurs into database using prepared statement
     *
     * @param kurs
     */
    public void insertKurs(Kurs kurs){
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO kurs (kId,bezeichnung,raum) "+
                            "Values(?,?,?);");
            preparedStatement.setInt(1,kurs.getKId());
            preparedStatement.setString(2,kurs.getBezeichnung());
            preparedStatement.setString(3,kurs.getRaum());
            preparedStatement.execute();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     * updates a kurs in database
     *
     * @param kurs
     */
    public void updateKurs(Kurs kurs){
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "UPDATE kurs SET bezeichnung = ?,raum = ?"+
                            "WHERE kId = "+kurs.getKId()+";"
            );
            preparedStatement.setString(1,kurs.getBezeichnung());
            preparedStatement.setString(2,kurs.getRaum());
            preparedStatement.execute();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public void deleteKurs(Kurs kurs){
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "UPDATE schueler SET kId = 1000 "+
                            "WHERE kId = ?;"
            );
            preparedStatement.setInt(1,kurs.getKId());
            preparedStatement.execute();
            PreparedStatement preparedStatement1;
            preparedStatement1 = connection.prepareStatement(
                    "DELETE FROM kurs "+
                            "WHERE kId = ?;"
            );
            preparedStatement.setInt(1,kurs.getKId());
            preparedStatement.execute();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     * inserts new unternehmen into database using prepared statement
     *
     * @param unternehmen
     */
    public void insertUnternehmen(Unternehmen unternehmen){
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO unternehmen (uId,name) "+
                            "Values(?,?);");
            preparedStatement.setInt(1,unternehmen.getUId());
            preparedStatement.setString(2,unternehmen.getName());
            preparedStatement.execute();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }


    /**
     * updates an unternehmen in database
     *
     * @param unternehmen
     */
    public void updateUnternehmen(Unternehmen unternehmen) {
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "UPDATE unternehmen SET name = ? "+
                            "WHERE uId = "+unternehmen.getUId()+";"
            );
            preparedStatement.setString(1,unternehmen.getName());
            preparedStatement.execute();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     * deletes an unternehmen from database
     *
     * @param unternehmen
     */
    public void deleteUnternehmen(Unternehmen unternehmen){
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "UPDATE schueler SET uId = 3 "+
                            "WHERE uId = ?;"
            );
            preparedStatement.setInt(1,unternehmen.getUId());
            preparedStatement.execute();
            PreparedStatement preparedStatement1;
            preparedStatement1 = connection.prepareStatement(
                    "DELETE FROM unternehmen "+
                            "WHERE uId = ?;"
            );
            preparedStatement.setInt(1,unternehmen.getUId());
            preparedStatement.execute();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
