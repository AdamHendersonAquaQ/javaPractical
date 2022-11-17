package com.aquaq.training.javaPractical.controllers;

import com.aquaq.training.javaPractical.classes.Course;
import com.aquaq.training.javaPractical.jdbc.CourseJdbcDao;
import org.springframework.beans.factory.annotation.Autowired;
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

    @DeleteMapping("/id/{id}")
    public String deleteCourse(@PathVariable(value = "id") int id) {
        return courseJdbcDao.deleteCourse(id);
    }

    @PutMapping("/update/")
    public String updateCourse(@RequestBody Course newCourse) {
        return courseJdbcDao.updateCourse(newCourse);
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
    public List<Course> findCourseById(@PathVariable(value = "id") int id) {
        return courseJdbcDao.findById(id);
    }

    @GetMapping("/subject/{subjectArea}")
    public List<Course> findCourseBySubjectArea(@PathVariable(value = "subjectArea") String subjectArea) {
        return courseJdbcDao.findBySubjectArea(subjectArea);
    }

    @PostMapping()
    public Course addCourse(@RequestBody Course course)
    {
        return courseJdbcDao.addNewCourse(course);
    }

    @GetMapping("/studentSemester/")
    public List<Course> getCoursesBySemester(@RequestParam int studentId, @RequestParam String semesterCode)
    {
        return courseJdbcDao.getCoursesBySemester(studentId,semesterCode);
    }
}
