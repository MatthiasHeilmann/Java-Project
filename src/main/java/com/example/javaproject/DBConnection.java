package com.example.javaproject;

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
               student1.update(student);
               this.setChanged();
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
     * updates a student in database
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
     * updates a kurs in database
     *
     * @param kurs
     */
    public void updateKurs(Kurs kurs){
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "UPDATE kurs SET bezeichnung = ?,raum = ?"+
                            "WHERE kId = "+kurs.getkId()+";"
            );
            preparedStatement.setString(1,kurs.getBezeichnung());
            preparedStatement.setString(2,kurs.getRaum());
            preparedStatement.execute();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     * updates or adds a kurs to ArrayList
     *
     * @param kurs
     */
    public void updateKursArrayList(Kurs kurs) {
        AtomicBoolean added= new AtomicBoolean(true);
        kursArrayList.forEach(kurs1 -> {
            if (kurs1.getkId()==kurs.getkId()){
                kurs.update(kurs1);
                added.set(false);
                this.setChanged();
                notifyObservers();
            }
        });
        if(added.get()){
            kursArrayList.add(kurs);
            this.setChanged();
            notifyObservers();
        }
    }

    public void deleteKurs(Kurs kurs){
        try{
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "UPDATE schueler SET kId = 1000 "+
                            "WHERE kId = ?;"
            );
            preparedStatement.setInt(1,kurs.getkId());
            preparedStatement.execute();
            PreparedStatement preparedStatement1;
            preparedStatement1 = connection.prepareStatement(
                    "DELETE FROM kurs "+
                            "WHERE kId = ?;"
            );
            preparedStatement.setInt(1,kurs.getkId());
            preparedStatement.execute();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public void deleteKursArrayList(Kurs kurs){
        kursArrayList.forEach(kurs1 -> {
            if (kurs1.getkId()==kurs.getkId()){
                kurs.update(new Kurs(1000,"kurslos", "NA"));
                updateKursArrayList(kurs);
                updateKurs(kurs);
                studentArrayList.forEach(student -> {
                    if(student.getkId()==kurs.getkId()){
                        student.setkId(1000);
                        student.setKurs("kurslos");
                    }
                });
                this.setChanged();
                notifyObservers();
            }
        });
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
            preparedStatement.setInt(1,unternehmen.getuId());
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
                            "WHERE uId = "+unternehmen.getuId()+";"
            );
            preparedStatement.setString(1,unternehmen.getName());
            preparedStatement.execute();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     * updates an unternehmen in ArrayList
     *
     * @param unternehmen
     */
    public void updateUnternehmenArrayList(Unternehmen unternehmen) {
        unternehmenArrayList.forEach(unternehmen1 -> {
            if (unternehmen1.getuId()==unternehmen.getuId()){
                unternehmen.update(unternehmen1);
                this.setChanged();
                notifyObservers();
            }
        });
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
            preparedStatement.setInt(1,unternehmen.getuId());
            preparedStatement.execute();
            PreparedStatement preparedStatement1;
            preparedStatement1 = connection.prepareStatement(
                    "DELETE FROM unternehmen "+
                            "WHERE uId = ?;"
            );
            preparedStatement.setInt(1,unternehmen.getuId());
            preparedStatement.execute();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     * deletes an unternehmen from ArrayList
     *
     * @param unternehmen
     */
    public void deleteUnternehmenArrayList(Unternehmen unternehmen){
        unternehmenArrayList.forEach(unternehmen1 -> {
            if (unternehmen1.getuId()==unternehmen.getuId()){
                unternehmen.update(new Unternehmen(3,"Others UG"));
                studentArrayList.forEach(student -> {
                    if(student.getuId()==unternehmen.getuId()){
                        student.setuId(3);
                        student.setUnternehmen("Others UG");
                    }
                });
                this.setChanged();
                notifyObservers();
            }
        });
    }
}
