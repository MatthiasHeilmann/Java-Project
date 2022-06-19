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
public class DBConnection{
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
            System.out.println("[DBConnection] Connection successfully created.");
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
    public int insertStudent(Schueler schueler){
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO schueler (uId,kId,nachname,vorname,geschlecht,vorkenntnisse) "+
                            "Values(?,?,?,?,?,?);",Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, schueler.getUId());
            preparedStatement.setInt(2, schueler.getKId());
            preparedStatement.setString(3, schueler.getNachname());
            preparedStatement.setString(4, schueler.getVorname());
            preparedStatement.setString(5, schueler.getGeschlecht());
            preparedStatement.setInt(6, schueler.getVorkenntnisse());
            preparedStatement.execute();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * updates a student in database
     *
     * @param schueler Schueler
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
     * Deletes a Schueler from database
     *
     * @param schueler Schueler
     */
    public void deleteStudent(Schueler schueler){
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "DELETE FROM schueler "+
                            "WHERE sId = ?;"
            );
            preparedStatement.setInt(1,schueler.getSId());
            preparedStatement.execute();
            System.out.println("Schueler "+schueler.getVorname()+" wurde vor der DHBW in Sicherheit gebracht.");
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     * inserts new kurs into database using prepared statement
     *
     * @param kurs Kurs
     */
    public int insertKurs(Kurs kurs){
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO kurs (kId,bezeichnung,raum) "+
                            "Values(?,?,?);",Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,kurs.getKId());
            preparedStatement.setString(2,kurs.getBezeichnung());
            preparedStatement.setString(3,kurs.getRaum());
            preparedStatement.execute();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating kurs failed, no ID obtained.");
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * updates a kurs in database
     *
     * @param kurs Kurs
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

    /**
     * Deletes a Kurs from database
     *
     * @param kurs Kurs
     */
    public void deleteKurs(Kurs kurs){
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
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
     * @param unternehmen Unternehmen
     */
    public int insertUnternehmen(Unternehmen unternehmen){
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO unternehmen (uId,name) "+
                            "Values(?,?);",Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,unternehmen.getUId());
            preparedStatement.setString(2,unternehmen.getName());
            preparedStatement.execute();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return 0;
    }


    /**
     * updates an unternehmen in database
     *
     * @param unternehmen Unternehmen
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
     * @param unternehmen Unternehmen
     */
    public void deleteUnternehmen(Unternehmen unternehmen){
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "DELETE FROM unternehmen "+
                            "WHERE uId = ?;"
            );
            preparedStatement.setInt(1,unternehmen.getUId());
            preparedStatement.execute();
            System.out.println("Unternehmen wurde aus db entfernt.");
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Closes the Connection to database
     */
    public void closeConnection(){
        try {
            this.connection.close();
            System.out.println("[DBConnection] Connection closed.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
