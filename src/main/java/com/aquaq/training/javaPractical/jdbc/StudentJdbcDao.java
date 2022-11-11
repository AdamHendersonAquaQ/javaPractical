package com.aquaq.training.javaPractical.jdbc;

import com.aquaq.training.javaPractical.classes.Student;
import com.aquaq.training.javaPractical.errorHandling.StudentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class StudentJdbcDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    GeneratedKeyHolderFactory keyHolderFactory;

    private static final Logger logger = Logger.getLogger(CourseJdbcDao.class.getName());
    
    public List<Student> findAll() {
        logger.log(Level.INFO,"Finding all students");
        List<Student> students = jdbcTemplate.query("select * from Student",
                new BeanPropertyRowMapper<>(Student.class));
        if (students.size() != 0)
            return students;
        else
            throw throwStudentError("No students found");
    }

    public String deleteStudent(int studentId) {
        logger.log(Level.INFO,"Deleting student id: "+studentId);
        String sql = "DELETE FROM studentCourse WHERE studentId = ?";
        jdbcTemplate.update(sql,studentId);
        sql = "DELETE FROM Student WHERE studentId = ?";
        int returnVal = jdbcTemplate.update(sql,studentId);
        if(returnVal>0)
            return "Student has been successfully deleted.";
        else
            throw throwStudentError("Student deletion with Student ID: " + studentId + " failed");
    }

    public String updateStudent(Student student) {
        checkStudent(student);
        logger.log(Level.INFO,"Updating student with id: " + student.getStudentId());
        String sql = ("UPDATE Student SET firstName=?, lastName=?,graduationYear=? WHERE studentId=?");
        int returnVal = jdbcTemplate.update(sql, student.getFirstName(), student.getLastName(),
                student.getGraduationYear(), student.getStudentId() );
        if (returnVal == 1)
            return "Student has been successfully updated. ";
        else
            throw throwStudentError("Updating student with ID: " + student.getStudentId() + " has failed");
    }

    public List<Student> findById(int id) {
        logger.log(Level.INFO,"Finding student with id: " + id);
        List<Student> students = jdbcTemplate.query("select * from Student where studentId=?",
                new BeanPropertyRowMapper<>(Student.class), id);
        if (students.size() != 0)
            return students;
        else
            throw throwStudentError("Student ID not found - " + id);
    }

    public List<Student> findByStudentName(String firstName, String lastName) {
        List<Student> students;
        logger.log(Level.INFO,"Finding student with name: " + firstName+ " "+lastName);
        if(Objects.equals(firstName, "") && !Objects.equals(lastName, ""))
            students = jdbcTemplate.query("select * from Student where lastName = ?", new BeanPropertyRowMapper<>(Student.class), lastName);
        else if(!Objects.equals(firstName, "") && Objects.equals(lastName, ""))
            students = jdbcTemplate.query("select * from Student where firstName = ?", new BeanPropertyRowMapper<>(Student.class), firstName);
        else if (!Objects.equals(firstName, "") && !Objects.equals(lastName, ""))
            students = jdbcTemplate.query("select * from Student where firstName = ? AND lastName = ?", new BeanPropertyRowMapper<>(Student.class), firstName, lastName);
        else
            throw throwStudentError("At least one name field must not be blank");
        if (students.size() != 0)
            return students;
        else
            throw throwStudentError("Student name not found - " + firstName + " " + lastName);
    }

    public List<Student> findBySemester(String semesterCode) {
        if (semesterCode.matches("^[A-Z]+[0-9]{4}$")) {
            logger.log(Level.INFO,"Finding student enrolled in semester: " + semesterCode);
            List<Student> students = jdbcTemplate.query("SELECT DISTINCT Student.StudentId, FirstName, LastName, " +
                    "GraduationYear, SemesterCode FROM Student LEFT JOIN StudentCourse ON " +
                    "Student.StudentId = StudentCourse.StudentId LEFT JOIN Course ON " +
                    "StudentCourse.CourseId = Course.CourseId WHERE Course.SemesterCode = ?",
                    new BeanPropertyRowMapper<>(Student.class), semesterCode);
            if(students.size() != 0)
                return students;
            else
                throw throwStudentError("Not students found for Semester - " + semesterCode);
        } else
            throw throwStudentError("Semester not found - " + semesterCode);
    }

    public Student addNewStudent(Student student) {
        checkStudent(student);
        KeyHolder keyHolder = keyHolderFactory.newKeyHolder();
        String sql = ("INSERT INTO Student (firstName,lastName,graduationYear) " +
                "VALUES (?,?,?)");
        logger.log(Level.INFO,"Adding new student");
        jdbcTemplate.update(c -> prepareStatement(sql,c,student),keyHolder);
        student.setStudentId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return student;
    }

    public void checkStudent(Student student) {
        if(student.getFirstName()==null||student.getFirstName().isEmpty())
            throw throwStudentError("Student cannot be created with no first name");
        if(student.getGraduationYear()< LocalDateTime.now().getYear())
            throw throwStudentError("Student graduation year must be in the future");
        if(!student.getFirstName().matches("[a-zA-Z]+") || !student.getLastName().matches("[a-zA-Z]+"))
            throw throwStudentError("Student name must only be letters");

    }

    private PreparedStatement prepareStatement(String sql, Connection c, Student student) throws SQLException {
        PreparedStatement ps = c
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, student.getFirstName());
        ps.setString(2, student.getLastName());
        ps.setInt(3, student.getGraduationYear());
        return ps;
    }
    
    public static StudentNotFoundException throwStudentError(String errorMsg)
    {
        logger.log(Level.WARNING, errorMsg);
        return new StudentNotFoundException(errorMsg);
    }
}


