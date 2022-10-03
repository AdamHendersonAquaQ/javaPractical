package com.aquaq.training.javaPractical;

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

    @GetMapping("studentName/")
    public Student findStudentByName(@RequestParam String firstName, @RequestParam String lastName) {
        return studentJdbcDao.findByStudentName(firstName, lastName);
    }


    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleStudentException(StudentNotFoundException exc) {
        StudentErrorResponse error = new StudentErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(Exception exc) {
        StudentErrorResponse error = new StudentErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
