package com.example.javaproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Model class of project
 * DBConnection class creates a connection to database and communicates with it
 */
public class DBConnection extends Observable {
    private Connection connection;
    private final String connectionUrl = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11495010";
    private final String userName = "sql11495010";
    private final String password = "SRwSuf13UR";
    private ArrayList<Student> studentArrayList;
    private ArrayList<Kurs> kursArrayList;
    private ArrayList<Unternehmen> unternehmenArrayList;

    public DBConnection(){
        createConnection();
        studentArrayList=getAllSchueler();
        kursArrayList=getAllKurs();
        unternehmenArrayList=getAllUnternehmen();
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
     * gets all Students from database ordered by vorname
     *
     * @return ArrayList<Student>
     */
    private ArrayList<Student> getAllSchueler(){
        ArrayList<Student> arrayList = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM schueler ORDER BY vorname;");
            while (resultSet.next()){
                arrayList.add(new Student(
                        resultSet.getInt("sId"),
                        resultSet.getInt("uId"),
                        resultSet.getInt("kId"),
                        resultSet.getString("vorname"),
                        resultSet.getString("nachname"),
                        resultSet.getString("geschlecht"),
                        resultSet.getInt("vorkenntnisse")
                ));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return arrayList;
    }

    /**
     * gets all Kurs from database
     *
     * @return ArrayList<Kurs>
     */
    private ArrayList<Kurs> getAllKurs(){
        ArrayList<Kurs> arrayList = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM kurs;");
            while (resultSet.next()){
                arrayList.add(new Kurs(
                        resultSet.getInt("kId"),
                        resultSet.getString("bezeichnung"),
                        resultSet.getString("raum")
                ));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return arrayList;
    }

    /**
     * gets all Unternehmen from database
     *
     * @return ArrayList<Unternehmen>
     */
    private ArrayList<Unternehmen> getAllUnternehmen(){
        ArrayList<Unternehmen> arrayList = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM unternehmen;");
            while (resultSet.next()){
                arrayList.add(new Unternehmen(
                        resultSet.getInt("uId"),
                        resultSet.getString("name")
                ));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return arrayList;
    }

    public ArrayList<Student> getStudentArrayList() {
        return studentArrayList;
    }

    public ArrayList<Kurs> getKursArrayList() {
        return kursArrayList;
    }

    public ArrayList<Unternehmen> getUnternehmenArrayList() {
        return unternehmenArrayList;
    }

    /**
     * updates the student arraylist
     *
     * @param student
     */
    public void updateStudentArrayList(Student student){
        studentArrayList.forEach(student1 -> {
           if (student1.getsId()==student.getsId()){
               student.update(student1);
               notifyObservers();
           }
        });
    }

    /**
     * inserts a Student to database using prepared statement
     *
     * @param student
     */
    public void insertStudent(Student student){
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO schueler (uId,kId,nachname,vorname,geschlecht,vorkenntnisse) "+
                            "Values(?,?,?,?,?,?);");
            preparedStatement.setInt(1,student.getuId());
            preparedStatement.setInt(2,student.getkId());
            preparedStatement.setString(3,student.getNachname());
            preparedStatement.setString(4,student.getVorname());
            preparedStatement.setString(5,student.getGeschlecht());
            preparedStatement.setInt(6,student.getVorkenntnisse());
            preparedStatement.execute();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     * updates a student from database
     *
     * @param student
     */
    public void updateStudent(Student student){
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "UPDATE schueler SET uId = ?,kId = ?,nachname = ?,vorname = ?,geschlecht = ?,vorkenntnisse = ? "+
                            "WHERE sId = "+student.getsId()+";"
            );
            preparedStatement.setInt(1,student.getuId());
            preparedStatement.setInt(2,student.getkId());
            preparedStatement.setString(3,student.getNachname());
            preparedStatement.setString(4,student.getVorname());
            preparedStatement.setString(5,student.getGeschlecht());
            preparedStatement.setInt(6,student.getVorkenntnisse());
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
            preparedStatement.setInt(1,kurs.getkId());
            preparedStatement.setString(2,kurs.getBezeichnung());
            preparedStatement.setString(3,kurs.getRaum());
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
    public void insertKurs(Unternehmen unternehmen){
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO unternehmen (uId,name) "+
                            "Values(?,?);");
            preparedStatement.setInt(1,unternehmen.getuId());
            preparedStatement.setString(2,unternehmen.getName());
            preparedStatement.execute();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}