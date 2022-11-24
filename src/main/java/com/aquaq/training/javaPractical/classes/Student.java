package com.aquaq.training.javaPractical.classes;

public class Student {

    private int studentId;
    private String firstName;
    private String lastName;
    private int graduationYear;

    public Student()
    {}
    public Student(int studentId, String firstName, String lastName, int graduationYear){
        this.studentId=studentId;
        this.firstName=firstName;
        this.lastName=lastName;
        this.graduationYear=graduationYear;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }
}
