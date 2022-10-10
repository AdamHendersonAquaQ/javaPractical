package com.aquaq.training.javaPractical.controllers;

import com.aquaq.training.javaPractical.classes.Student;
import com.aquaq.training.javaPractical.errorHandling.StudentErrorResponse;
import com.aquaq.training.javaPractical.errorHandling.StudentNotFoundException;
import com.aquaq.training.javaPractical.jdbc.StudentJdbcDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentJdbcDao studentJdbcDao;

    @GetMapping
    public List<Student> findAllStudents()
    {
        return studentJdbcDao.findAll();
    }

    @GetMapping("/id/{id}")
    public Student findStudentById(@PathVariable(value = "id") int id) {
        return studentJdbcDao.findById(id);
    }

    @GetMapping("/studentName/")
    public Student findStudentByName(@RequestParam String firstName, @RequestParam String lastName) {
        return studentJdbcDao.findByStudentName(firstName, lastName);
    }

    @GetMapping("/semester/{semesterCode}")
    public List<Student> findStudentsBySemester(@PathVariable(value = "semesterCode") String semesterCode) {
        return studentJdbcDao.findBySemester(semesterCode);
    }

    @PostMapping("/addStudent/")
    public Student addStudent(@RequestBody Student student)
    {
        return studentJdbcDao.addNewStudent(student);
    }

    @PostMapping("/enrollStudent/")
    public String enrollStudent(@RequestParam String studentId, @RequestParam String courseId) {
        return studentJdbcDao.enrollStudent(Integer.parseInt(studentId), Integer.parseInt(courseId));
    }

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleStudentException(StudentNotFoundException exc) {
        return new ResponseEntity<>(new StudentErrorResponse(HttpStatus.NOT_FOUND.value(),
                exc.getMessage(),System.currentTimeMillis()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(Exception exc) {
        return new ResponseEntity<>(new StudentErrorResponse(HttpStatus.BAD_REQUEST.value(),
                exc.getMessage(),System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
    }
}
