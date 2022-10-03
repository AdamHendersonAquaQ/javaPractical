package com.aquaq.training.javaPractical;

import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentTest {

    @Test
    public void studentIdTest()
    {
        Student student = new Student();
        student.setStudentId(12);
        assertEquals(student.getStudentId(),12);
    }

    @Test
    public void studentFirstNameTest()
    {
        Student student = new Student();
        student.setFirstName("Lorna");
        assertEquals(student.getFirstName(),"Lorna");
    }

    @Test
    public void studentLastNameTest()
    {
        Student student = new Student();
        student.setLastName("Dane");
        assertEquals(student.getLastName(),"Dane");
    }

    @Test
    public void studentGraduationYearTest()
    {
        Student student = new Student();
        student.setGraduationYear(new Date(01012023));
        assertEquals(student.getGraduationYear(),new Date(01012023));
    }

    @Test
    public void toStringTest()
    {
        Student student = new Student(12,"Lorna","Dane",new Date(01012023));
        assertEquals(student.toString().trim(), "Person [id=12, firstName=Lorna, lastName=Dane, graduationYear=1970-01-01]".trim());
    }

}
