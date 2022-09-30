package com.aquaq.training.javaPractical;

import com.aquaq.training.javaPractical.jdbc.StudentJdbcDao;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/{id}")
    public Student findStudentById(@PathVariable(value = "id") int id) {
        return studentJdbcDao.findById(id);
    }


}
