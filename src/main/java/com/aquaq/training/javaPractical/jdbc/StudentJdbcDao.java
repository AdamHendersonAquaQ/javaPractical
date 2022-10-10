package com.aquaq.training.javaPractical.jdbc;

import com.aquaq.training.javaPractical.classes.Course;
import com.aquaq.training.javaPractical.classes.Student;
import com.aquaq.training.javaPractical.controllers.CourseController;
import com.aquaq.training.javaPractical.errorHandling.StudentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class StudentJdbcDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    GeneratedKeyHolderFactory keyHolderFactory;

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
            List<Student> students = jdbcTemplate.query("SELECT DISTINCT Student.StudentId, FirstName, LastName, " +
                    "GraduationYear, SemesterCode FROM Student LEFT JOIN StudentCourse ON " +
                    "Student.StudentId = StudentCourse.StudentId LEFT JOIN Course ON " +
                    "StudentCourse.CourseId = Course.CourseId WHERE Course.SemesterCode = ?",
                    new BeanPropertyRowMapper(Student.class), semesterCode);
            if(students.size() == 0)
                throw new StudentNotFoundException("Not students found for semester - " + semesterCode);
            else
                return students;
        } else
            throw new StudentNotFoundException("Semester not found - " + semesterCode);
    }

    public Student addNewStudent(Student student) {
        if(student.getFirstName()==null||student.getFirstName().isEmpty())
            throw new StudentNotFoundException("Student cannot be created with no first name");
        KeyHolder keyHolder = keyHolderFactory.newKeyHolder();
        String sql = ("INSERT INTO Student (firstName,lastName,graduationYear) " +
                "VALUES (?,?,?)");
        jdbcTemplate.update(c -> {
            PreparedStatement ps = c
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setInt(3, student.getGraduationYear());
            return ps;
        },keyHolder);
        student.setStudentId(keyHolder.getKey().intValue());
        return student;
    }

    public String enrollStudent(int studentId, int courseId) {
        //Get course info
        CourseController courseController = new CourseController();
        Course course = courseController.findCourseById(courseId);
        //Check student semester credits
//        int semesterCredits = getStudentCredits(course.getSemesterCode());
        //Check course capacity

        String sql = ("INSERT INTO StudentCourse (studentId, courseId) " +
                "VALUES (?,?)");
        int returnVal =  jdbcTemplate.update(c -> {
            PreparedStatement ps = c
                    .prepareStatement(sql);
            ps.setInt(1,studentId);
            ps.setInt(2,courseId);
            return ps;
        });
        if(returnVal==1)
            return "Student has been successfully registered";
        else
            throw new StudentNotFoundException("Student could not be registered in course");
    }

//    public int getStudentCredits(String semesterCode)
//    {
//        if (semesterCode.matches("^[A-Z]+[0-9]{4}$")) {
//            List<Student> students = jdbcTemplate.query("", new BeanPropertyRowMapper(Student.class), semesterCode);
//            if(students.size() == 0)
//                throw new StudentNotFoundException("Not students found for semester - " + semesterCode);
//            else
//                return students;
//        } else
//            throw new StudentNotFoundException("Semester not found - " + semesterCode);
//
//    }
}


