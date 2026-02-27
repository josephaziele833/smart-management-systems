package com.template.domain;

import java.time.LocalDate;

public class Student {

    private String studentId;
    private String fullName;
    private String programme;
    private int level;
    private double gpa;
    private String email;
    private String phoneNumber;
    private LocalDate dateAdded;
    private String status; // Active or Inactive

    public Student(String studentId, String fullName, String programme,
                   int level, double gpa, String email,
                   String phoneNumber, LocalDate dateAdded, String status) {

        this.studentId = studentId;
        this.fullName = fullName;
        this.programme = programme;
        this.level = level;
        this.gpa = gpa;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateAdded = dateAdded;
        this.status = status;
    }

    public Student(String field, String field1, String field2, int level, double gpa, String field3) {
    }

    // Getters

    public String getStudentId() {
        return studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getProgramme() {
        return programme;
    }

    public int getLevel() {
        return level;
    }

    public double getGpa() {
        return gpa;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public String getStatus() {
        return status;
    }

    // Setters (needed for update)

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }
}