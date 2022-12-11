package com.project.androidlab.db;

public class University {

    private int id;
    private String universityName;
    private String city;
    private int studentsNumber;
    private int rating;

    public University(int id, String universityName, String city, int studentsNumber, int rating) {
        this.id = id;
        this.universityName = universityName;
        this.city = city;
        this.studentsNumber = studentsNumber;
        this.rating = rating;
    }

    public University(String universityName, String city, int studentsNumber, int rating) {
        this.universityName = universityName;
        this.city = city;
        this.studentsNumber = studentsNumber;
        this.rating = rating;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getStudentsNumber() {
        return studentsNumber;
    }

    public void setStudentsNumber(int studentsNumber) {
        this.studentsNumber = studentsNumber;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
