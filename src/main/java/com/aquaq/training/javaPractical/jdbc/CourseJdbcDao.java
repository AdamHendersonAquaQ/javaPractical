package com.aquaq.training.javaPractical.jdbc;

import com.aquaq.training.javaPractical.classes.Course;
import com.aquaq.training.javaPractical.errorHandling.CourseNotFoundException;
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
public class CourseJdbcDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    GeneratedKeyHolderFactory keyHolderFactory;

    private static final Logger logger = Logger.getLogger(CourseJdbcDao.class.getName());

    public List<Course> findAll() {
        logger.log(Level.INFO,"Finding all courses");
        List<Course> courses = jdbcTemplate.query("select * from Course",
                new BeanPropertyRowMapper<>(Course.class));
        if (courses.size() == 0)
            throw throwCourseError("No courses found");
        else
            return courses;
    }

    public String deleteCourse(int courseId) {
        logger.log(Level.INFO,"Deleting course id: "+courseId);
        String sql = "DELETE FROM studentCourse WHERE courseId = ?";
        jdbcTemplate.update(sql,courseId);
        sql = "DELETE FROM Course WHERE courseId = ?";
        int returnVal = jdbcTemplate.update(sql,courseId);
        if(returnVal>0)
            return "Course has been successfully deleted.";
        else
            throw throwCourseError("Course deletion with course id: " + courseId + " failed");
    }

    public String updateCourse(Course course) {
        if(course.getCourseName()==null||course.getCourseName().isEmpty())
            throw throwCourseError("Course cannot be set with no name");
        logger.log(Level.INFO,"Updating course with id: " + course.getCourseId());
        String sql = ("UPDATE Course SET CourseName=?, subjectArea=?,creditAmount=?,studentCapacity=?," +
                "semesterCode=? WHERE courseId=?");
        int returnVal = jdbcTemplate.update(sql, course.getCourseName(), course.getSubjectArea(),
                course.getCreditAmount(), course.getStudentCapacity(), course.getSemesterCode(),
                course.getCourseId() );
        if (returnVal == 1)
            return "Course has been successfully updated. ";
        else
            throw throwCourseError("Updating course with id: " + course.getCourseId() + " has failed");
    }

    public List<Course> findBySemester(String semesterCode) {
        logger.log(Level.INFO,"Finding courses for semester " + semesterCode);
        List<Course> courses = jdbcTemplate.query("select * from Course where semesterCode=?",
                new BeanPropertyRowMapper<>(Course.class), semesterCode);
        if (courses.size() != 0)
            return courses;
        else
            throw throwCourseError("No courses found for semester: " + semesterCode);
    }

    public List<Course> findByCourseName(String courseName) {
        logger.log(Level.INFO,"Finding courses for name " + courseName);
        List<Course> courses = jdbcTemplate.query("select * from Course where courseName=?",
                new BeanPropertyRowMapper<>(Course.class), courseName);
        if (courses.size() != 0)
            return courses;
        else
            throw throwCourseError("No courses found for name: " + courseName);
    }

    public List<Course> findById(int id) {
        logger.log(Level.INFO,"Finding courses for id " + id);
        List<Course> courses = jdbcTemplate.query("select * from Course where courseId=?",
                new BeanPropertyRowMapper<>(Course.class), id);
        if (courses.size()>0)
            return courses;
        else
            throw throwCourseError("No courses found for id: " + id);
    }

    public List<Course> findBySubjectArea(String subjectArea) {
        logger.log(Level.INFO,"Finding courses for subjectArea " + subjectArea);
        List<Course> courses = jdbcTemplate.query("select * from Course where subjectArea=?",
                new BeanPropertyRowMapper<>(Course.class), subjectArea);
        if (courses.size() != 0)
            return courses;
        else
            throw throwCourseError("No courses with this subject area found - " + subjectArea);
    }
    
    public Course addNewCourse(Course course) {
        if(course.getCourseName()==null||course.getCourseName().isEmpty())
            throw throwCourseError("Course cannot be created with no name");
        KeyHolder keyHolder = keyHolderFactory.newKeyHolder();
        String sql = ("INSERT INTO Course (courseName,subjectArea,creditAmount,studentCapacity,semesterCode) " +
                "VALUES (?,?,?,?,?)");
        logger.log(Level.INFO,"Adding new course");
        jdbcTemplate.update(c -> prepareStatement(sql,c,course),keyHolder);
        course.setCourseId(keyHolder.getKey().intValue());
        return course;
    }

    private PreparedStatement prepareStatement(String sql, Connection c,Course course) throws SQLException {
        PreparedStatement ps = c
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, course.getCourseName());
        ps.setString(2, course.getSubjectArea());
        ps.setInt(3, course.getCreditAmount());
        ps.setInt(4, course.getStudentCapacity());
        ps.setString(5, course.getSemesterCode());
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
            throw throwCourseError("No courses found for this student in semester "+semesterCode);
    }

    public static CourseNotFoundException throwCourseError(String errorMsg)
    {
        logger.log(Level.WARNING, errorMsg);
        return new CourseNotFoundException(errorMsg);
    }
}
