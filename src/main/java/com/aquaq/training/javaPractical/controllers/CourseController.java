package com.aquaq.training.javaPractical.controllers;

import com.aquaq.training.javaPractical.classes.Course;
import com.aquaq.training.javaPractical.errorHandling.CourseErrorResponse;
import com.aquaq.training.javaPractical.errorHandling.CourseNotFoundException;
import com.aquaq.training.javaPractical.jdbc.CourseJdbcDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseJdbcDao courseJdbcDao;

    @PostMapping("/addCourse/")
    public Course addCourse(@RequestBody Course course)
    {
        return courseJdbcDao.addNewCourse(course);
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
