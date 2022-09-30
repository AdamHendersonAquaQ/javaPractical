package com.aquaq.training.javaPractical.jdbc;

import com.aquaq.training.javaPractical.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentJdbcDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Student> findAll() {
        return jdbcTemplate.query("select * from Student",
                new BeanPropertyRowMapper<>(Student.class));
    }

    public Student findById(int id) {
        return (Student) jdbcTemplate.query("select * from Student where studentId=?",
                new BeanPropertyRowMapper<>(Student.class), id).get(0);
    }

    public List<Student> findBySemester(String semesterCode) {
        return jdbcTemplate.query("SELECT DISTINCT Student.StudentId, FirstName, LastName, " +
                "GraduationYear, SemesterCode FROM Student LEFT JOIN StudentCourse ON " +
                "Student.StudentId = StudentCourse.StudentId LEFT JOIN Course ON " +
                "StudentCourse.CourseId = Course.CourseId WHERE Course.SemesterCode = ?",
                new BeanPropertyRowMapper<>(Student.class), semesterCode);
    }

}
