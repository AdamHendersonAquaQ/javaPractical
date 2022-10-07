package com.aquaq.training.javaPractical.jdbc;

import com.aquaq.training.javaPractical.classes.Course;
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
}
