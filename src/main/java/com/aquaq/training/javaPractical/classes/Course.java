package com.aquaq.training.javaPractical.classes;

public class Course {
    private int courseId;
    private String courseName;
    private String subjectArea;
    private int creditAmount;
    private int studentCapacity;
    private String semesterCode;

    public Course()
    {}

    public Course(int courseId, String courseName, String subjectArea,
                  int creditAmount, int studentCapacity, String semesterCode) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.subjectArea = subjectArea;
        this.creditAmount = creditAmount;
        this.studentCapacity = studentCapacity;
        this.semesterCode = semesterCode;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSubjectArea() {
        return subjectArea;
    }

    public void setSubjectArea(String subjectArea) {
        this.subjectArea = subjectArea;
    }

    public int getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(int creditAmount) {
        this.creditAmount = creditAmount;
    }

    public int getStudentCapacity() {
        return studentCapacity;
    }

    public void setStudentCapacity(int studentCapacity) {
        this.studentCapacity = studentCapacity;
    }

    public String getSemesterCode() {
        return semesterCode;
    }

    public void setSemesterCode(String semesterCode) {
        this.semesterCode = semesterCode;
    }
}

