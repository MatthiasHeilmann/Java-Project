package com.example.javaproject;

/**
 * Student class
 */
public class Student {

    private int sId;
    private int uId;
    private int kId;
    private String vorname;
    private String nachname;
    private String geschlecht;
    private int vorkenntnisse;
    private String kurs;
    private String unternehmen;

    public Student(int sId, int uId, int kId, String vorname, String nachname, String geschlecht, int vorkenntnisse) {
        this.sId = sId;
        this.uId = uId;
        this.kId = kId;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geschlecht = geschlecht;
        this.vorkenntnisse = vorkenntnisse;
    }

    public Student(Student student){
        this.sId=student.getsId();
        this.uId=student.getuId();
        this.kId=student.getkId();
        this.vorname=student.getVorname();
        this.nachname=student.getNachname();
        this.geschlecht=student.getGeschlecht();
        this.vorkenntnisse=student.getVorkenntnisse();
        this.unternehmen=student.getUnternehmen();
        this.kurs=student.getKurs();
    }

    public void update(Student student){
        this.sId=student.getsId();
        this.uId=student.getuId();
        this.kId=student.getkId();
        this.vorname=student.getVorname();
        this.nachname=student.getNachname();
        this.geschlecht=student.getGeschlecht();
        this.vorkenntnisse=student.getVorkenntnisse();
        this.unternehmen=student.getUnternehmen();
        this.kurs=student.getKurs();
    }

    public int getsId() {
        return sId;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public int getkId() {
        return kId;
    }

    public void setkId(int kId) {
        this.kId = kId;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(String geschlecht) {
        this.geschlecht = geschlecht;
    }

    public int getVorkenntnisse() {
        return vorkenntnisse;
    }

    public void setVorkenntnisse(int vorkenntnisse) {
        this.vorkenntnisse = vorkenntnisse;
    }

    public String getKurs() {
        return kurs;
    }

    public void setKurs(String kurs) {
        this.kurs = kurs;
    }

    public String getUnternehmen() {
        return unternehmen;
    }

    public void setUnternehmen(String unternehmen) {
        this.unternehmen = unternehmen;
    }
}
