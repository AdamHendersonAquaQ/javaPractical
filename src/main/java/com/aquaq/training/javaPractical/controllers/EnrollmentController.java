package com.aquaq.training.javaPractical.controllers;

import com.aquaq.training.javaPractical.classes.Enrollment;
import com.aquaq.training.javaPractical.jdbc.EnrollmentJdbcDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {
    @Autowired
    EnrollmentJdbcDao enrollmentJdbcDao;

    @GetMapping
    public List<Enrollment> getEnrollments() {
        return enrollmentJdbcDao.findAll();
    }

    @GetMapping("/student/{id}")
    public List<Enrollment> getStudentEnrollment(@PathVariable(value="id")int id) {
        return enrollmentJdbcDao.findByStudent(id);
    }

    @GetMapping("/course/{id}")
    public List<Enrollment> getCourseEnrollment(@PathVariable(value="id")int id) {
        return enrollmentJdbcDao.findByCourse(id);
    }

    @GetMapping("/record/")
    public List<Enrollment> getEnrollment(@RequestParam int studentId, @RequestParam int courseId) {
        return enrollmentJdbcDao.findEnrollment(studentId,courseId);
    }

    @PostMapping()
    public String enrollStudent(@RequestParam int studentId, @RequestParam int courseId) {
        return enrollmentJdbcDao.enrollStudent(studentId, courseId);
    }

    @DeleteMapping()
    public String unEnrollStudent(@RequestParam int courseId, @RequestParam int studentId)
    {
        return enrollmentJdbcDao.unEnrollStudent(courseId,studentId);
    }
}
