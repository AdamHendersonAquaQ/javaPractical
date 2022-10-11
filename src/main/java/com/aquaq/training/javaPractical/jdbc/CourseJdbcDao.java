package com.aquaq.training.javaPractical.jdbc;

import com.aquaq.training.javaPractical.classes.Course;
import com.aquaq.training.javaPractical.errorHandling.CourseEnrollmentException;
import com.aquaq.training.javaPractical.errorHandling.CourseNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CourseJdbcDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    GeneratedKeyHolderFactory keyHolderFactory;

    public List<Course> findAll() {
        List<Course> courses = jdbcTemplate.query("select * from Course",
                new BeanPropertyRowMapper<>(Course.class));
        if (courses.size() == 0)
            throw new CourseNotFoundException("No courses found");
        else
            return courses;
    }

    public List<Course> findBySemester(String semesterCode) {
        List<Course> courses = jdbcTemplate.query("select * from Course where semesterCode=?",
                new BeanPropertyRowMapper<>(Course.class), semesterCode);
        if (courses.size() == 0)
            throw new CourseNotFoundException("No courses in this semester found - " + semesterCode);
        else
            return courses;
    }

    public List<Course> findByCourseName(String courseName) {
        List<Course> courses = jdbcTemplate.query("select * from Course where courseName=?",
                new BeanPropertyRowMapper<>(Course.class), courseName);
        if (courses.size() == 0)
            throw new CourseNotFoundException("No courses with this name found - " + courseName);
        else
            return courses;
    }

    public Course findById(int id) {
        Course course = jdbcTemplate.queryForObject("select * from Course where courseId=?",
                new BeanPropertyRowMapper<>(Course.class), id);
        if (course.getCourseName() == null || course.getCourseName().isEmpty())
            throw new CourseNotFoundException("No courses with this id found - " + id);
        else
            return course;
    }

    public List<Course> findBySubjectArea(String subjectArea) {
        List<Course> courses = jdbcTemplate.query("select * from Course where subjectArea=?",
                new BeanPropertyRowMapper<>(Course.class), subjectArea);
        if (courses.size() == 0)
            throw new CourseNotFoundException("No courses with this subject area found - " + subjectArea);
        else
            return courses;
    }
    
    public Course addNewCourse(Course course) {
        if(course.getCourseName()==null||course.getCourseName().isEmpty())
            throw new CourseNotFoundException("Course cannot be created with no name");
        KeyHolder keyHolder = keyHolderFactory.newKeyHolder();
        String sql = ("INSERT INTO Course (courseName,subjectArea,creditAmount,studentCapacity,semesterCode) " +
                "VALUES (?,?,?,?,?)");
        jdbcTemplate.update(c -> {
            PreparedStatement ps = c
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getSubjectArea());
            ps.setInt(3, course.getCreditAmount());
            ps.setInt(4, course.getStudentCapacity());
            ps.setInt(5, course.getStudentCapacity());
            return ps;
        },keyHolder);
        course.setCourseId(keyHolder.getKey().intValue());
        return course;
    }

    public String enrollStudent(int studentId, int courseId) {
        if(!checkIfEnrolled(studentId,courseId)) {
            Course course = this.findById(courseId);
            int semesterCredits = getStudentCredits(course.getSemesterCode(), studentId);
            int newSemesterCredits = semesterCredits + course.getCreditAmount();
            if (newSemesterCredits <= 20) {
                int availableSpaces = course.getStudentCapacity() - getCurrentCourseCapacity(courseId);
                if (availableSpaces > 0)
                    return enrollStudentInCourse(studentId, courseId);
                else
                    throw new CourseEnrollmentException("Student could not be added, course capacity has been reached. ");
            } else
                throw new CourseEnrollmentException("Adding student would exceed their maximum course credits by "
                        + (newSemesterCredits - 20));
        }
        else
            throw new CourseEnrollmentException("Student is already enrolled in this course");
    }

    public boolean checkIfEnrolled(int studentId, int courseId)
    {
        Integer courseCountVar = jdbcTemplate.queryForObject("SELECT Count(studentId) " +
                        "FROM studentCourse WHERE studentId = ? AND courseId = ?",
                Integer.class, studentId, courseId );
        return courseCountVar != null && courseCountVar != 0;
    }

    public int getCurrentCourseCapacity(int courseId) {
        Integer courseCountVar = jdbcTemplate.queryForObject("SELECT COUNT(studentId) " +
                        "AS capacityCount FROM StudentCourse WHERE courseId = ?",
                Integer.class, courseId );
        if(courseCountVar==null)
            courseCountVar=0;
        return courseCountVar;
    }

    public int getStudentCredits(String semesterCode, int studentId)
    {
        if (semesterCode.matches("^[A-Z]+[0-9]{4}$")) {
            Integer courseCountVar = jdbcTemplate.queryForObject("SELECT Sum(creditAmount) as courseCount" +
                            " FROM Course LEFT JOIN StudentCourse ON StudentCourse.courseId = Course.courseId" +
                            " WHERE Course.SemesterCode = ? AND StudentId=?",
                    Integer.class, semesterCode, studentId );
            if(courseCountVar==null)
                courseCountVar=0;
            return courseCountVar;
        } else
            throw new CourseEnrollmentException("Semester not found - " + semesterCode);
    }

    public String enrollStudentInCourse(int studentId, int courseId)
    {
        String sql = ("INSERT INTO StudentCourse (studentId, courseId) " +
                "VALUES (?,?)");
        int returnVal = jdbcTemplate.update(sql,studentId, courseId);
        if (returnVal == 1)
            return "Student has been successfully registered";
        else
            throw new CourseEnrollmentException("Student could not be registered in course");
    }
}
