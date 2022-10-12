package com.aquaq.training.javaPractical.controllers;

import com.aquaq.training.javaPractical.classes.Course;
import com.aquaq.training.javaPractical.errorHandling.CourseErrorResponse;
import com.aquaq.training.javaPractical.errorHandling.CourseNotFoundException;
import com.aquaq.training.javaPractical.jdbc.CourseJdbcDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseJdbcDao courseJdbcDao;

    @GetMapping
    public List<Course> findAllCourses() {
        return courseJdbcDao.findAll();
    }

    @GetMapping("/semester/{semesterCode}")
    public List<Course> findCourseBySemester(@PathVariable(value = "semesterCode") String semesterCode) {
        return courseJdbcDao.findBySemester(semesterCode);
    }

    @GetMapping("/name/{courseName}")
    public List<Course> findCourseByName(@PathVariable(value = "courseName") String courseName) {
        return courseJdbcDao.findByCourseName(courseName);
    }

    @GetMapping("/id/{id}")
    public Course findCourseById(@PathVariable(value = "id") int id) {
        return courseJdbcDao.findById(id);
    }

    @GetMapping("/subject/{subjectArea}")
    public List<Course> findCourseBySubjectArea(@PathVariable(value = "subjectArea") String subjectArea) {
        return courseJdbcDao.findBySubjectArea(subjectArea);
    }

    @PostMapping("/addCourse/")
    public Course addCourse(@RequestBody Course course)
    {
        return courseJdbcDao.addNewCourse(course);
    }

    @PostMapping("/enrollStudent/")
    public String enrollStudent(@RequestParam String studentId, @RequestParam String courseId) {
        return courseJdbcDao.enrollStudent(Integer.parseInt(studentId), Integer.parseInt(courseId));
    }

    @ExceptionHandler
    public ResponseEntity<CourseErrorResponse> handleCourseException(CourseNotFoundException exc) {
        return new ResponseEntity<>(new CourseErrorResponse(HttpStatus.NOT_FOUND.value(),
                exc.getMessage(),System.currentTimeMillis()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<CourseErrorResponse> handleException(Exception exc) {
        return new ResponseEntity<>(new CourseErrorResponse(HttpStatus.BAD_REQUEST.value(),
                exc.getMessage(),System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
    }

}
