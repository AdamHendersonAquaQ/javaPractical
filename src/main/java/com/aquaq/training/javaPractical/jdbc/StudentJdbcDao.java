package com.aquaq.training.javaPractical.jdbc;

import com.aquaq.training.javaPractical.classes.Course;
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
import java.util.List;
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
            throw throwStudentError("Student deletion with student id: " + studentId + " failed");
    }

    public String updateStudent(Student student) {
        if(student.getFirstName()==null||student.getFirstName().isEmpty())
            throw throwStudentError("Student cannot be set with no first name");
        logger.log(Level.INFO,"Updating student with id: " + student.getStudentId());
        String sql = ("UPDATE Student SET firstName=?, lastName=?,graduationYear=? WHERE studentId=?");
        int returnVal = jdbcTemplate.update(sql, student.getFirstName(), student.getLastName(),
                student.getGraduationYear(), student.getStudentId() );
        if (returnVal == 1)
            return "Student has been successfully updated. ";
        else
            throw throwStudentError("Updating student with id: " + student.getStudentId() + " has failed");
    }

    public List<Student> findById(int id) {
        logger.log(Level.INFO,"Finding student with id: " + id);
        List<Student> students = jdbcTemplate.query("select * from Student where studentId=?",
                new BeanPropertyRowMapper<>(Student.class), id);
        if (students.size() != 0)
            return students;
        else
            throw throwStudentError("Student id not found - " + id);
    }

    public List<Student> findByStudentName(String firstName, String lastName) {
        logger.log(Level.INFO,"Finding student with name: " + firstName+ " "+lastName);
        List<Student> students = jdbcTemplate.query("select * from Student where firstName = ? AND lastName = ?",
                new BeanPropertyRowMapper<>(Student.class), firstName, lastName);
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
                throw throwStudentError("Not students found for semester - " + semesterCode);
        } else
            throw throwStudentError("Semester not found - " + semesterCode);
    }

    public Student addNewStudent(Student student) {
        if(student.getFirstName()==null||student.getFirstName().isEmpty())
            throw throwStudentError("Student cannot be created with no first name");
        KeyHolder keyHolder = keyHolderFactory.newKeyHolder();
        String sql = ("INSERT INTO Student (firstName,lastName,graduationYear) " +
                "VALUES (?,?,?)");
        logger.log(Level.INFO,"Adding new student");
        jdbcTemplate.update(c -> prepareStatement(sql,c,student),keyHolder);
        student.setStudentId(keyHolder.getKey().intValue());
        return student;
    }

    private PreparedStatement prepareStatement(String sql, Connection c, Student student) throws SQLException {
        PreparedStatement ps = c
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, student.getFirstName());
        ps.setString(2, student.getLastName());
        ps.setInt(3, student.getGraduationYear());
        return ps;
    }

    public List<Course> getCoursesBySemester(int studentId, String semesterCode)
    {
        String sql = "SELECT course.COURSEID, COURSENAME, SUBJECTAREA, CREDITAMOUNT, " +
                "STUDENTCAPACITY,SEMESTERCODE FROM Course " +
                "LEFT JOIN StudentCourse ON StudentCourse.courseId = Course.courseId " +
                "WHERE studentId=? AND semesterCode=?";
        List<Course> courses = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Course.class),
                studentId,semesterCode);
        if(courses.size()>0)
            return courses;
        else
            throw CourseJdbcDao.throwCourseError("No courses found for this student in semester "+semesterCode);
    }
    
    public static StudentNotFoundException throwStudentError(String errorMsg)
    {
        logger.log(Level.WARNING, errorMsg);
        return new StudentNotFoundException(errorMsg);
    }
}


