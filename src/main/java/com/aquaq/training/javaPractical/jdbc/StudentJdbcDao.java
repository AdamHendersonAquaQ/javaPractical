package com.aquaq.training.javaPractical.jdbc;

import com.aquaq.training.javaPractical.Student;
import com.aquaq.training.javaPractical.errorHandling.StudentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentJdbcDao {

    @Autowired
    JdbcTemplate jdbcTemplate;


    public List<Student> findAll() {
        List<Student> students = jdbcTemplate.query("select * from Student",
                new BeanPropertyRowMapper<>(Student.class));
        if (students.size() == 0)
            throw new StudentNotFoundException("No students found");
        else
            return students;
    }

    public Student findById(int id) {
        List<Student> students = jdbcTemplate.query("select * from Student where studentId=?",
                new BeanPropertyRowMapper<>(Student.class), id);
        if (students.size() == 0)
            throw new StudentNotFoundException("Student id not found - " + id);
        else
            return students.get(0);
    }

    public Student findByStudentName(String firstName, String lastName) {
        List<Student> students = jdbcTemplate.query("select * from Student where firstName = ? AND lastName = ?",
                new BeanPropertyRowMapper<>(Student.class), firstName, lastName);
        if (students.size() == 0)
            throw new StudentNotFoundException("Student name not found - " + firstName + " " + lastName);
        else
            return students.get(0);
    }

    public List<Student> findBySemester(String semesterCode) {
        if (semesterCode.matches("^[A-Z]+[0-9]{4}$")) {
            List<Student> students = jdbcTemplate.query("", new BeanPropertyRowMapper(Student.class), semesterCode);
            if(students.size() == 0)
                throw new StudentNotFoundException("Not students found for semester - " + semesterCode);
            else
                return students;
        } else
            throw new StudentNotFoundException("Semester not found - " + semesterCode);
    }

    @Autowired
    GeneratedKeyHolderFactory keyHolderFactory;

    public Student addNewStudent(Student student) {
        if(student.getFirstName()==null||student.getFirstName().isEmpty())
            throw new StudentNotFoundException("Student cannot be created with no first name");
        KeyHolder keyHolder = keyHolderFactory.newKeyHolder();
        String sql = ("INSERT INTO STUDENT (firstName,lastName,graduationYear) VALUES " +
                "(:firstName, :lastName, :graduationYear)");
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(":firstName",student.getFirstName());
        parameters.addValue(":lastName",student.getLastName());
        parameters.addValue(":graduationYear",student.getGraduationYear());
        jdbcTemplate.update(sql,parameters,keyHolder);
        student.setStudentId(keyHolder.getKey().intValue());
        return student;
    }
}


