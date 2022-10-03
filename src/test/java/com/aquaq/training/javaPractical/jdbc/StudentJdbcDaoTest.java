package com.aquaq.training.javaPractical.jdbc;

import com.aquaq.training.javaPractical.JavaPracticalApplication;
import com.aquaq.training.javaPractical.Student;
import com.aquaq.training.javaPractical.errorHandling.StudentNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JavaPracticalApplication.class)
public class StudentJdbcDaoTest {

    @InjectMocks
    StudentJdbcDao repository;
    @Mock
    JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads()
    {
    }

    @Test
    public void findAllTest()
    {
        List<Student> students = new ArrayList<>();
        students.add(new Student(6,"Sean","Cassidy",new Date(01012023)));

        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(students);

        List<Student> returnVal = repository.findAll();
        assertEquals(returnVal.size(),1);
        assertEquals(returnVal.get(0).getFirstName(),"Sean");
    }

    @Test
    public void findAllTest_fail()
    {
        List<Student> students = new ArrayList<>();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(students);

        assertThrows(StudentNotFoundException.class, () -> repository.findAll());
    }

    @Test
    public void findByIdTest()
    {
        List<Student> students = new ArrayList<>();
        students.add(new Student(7,"Kurt","Wagner",new Date(01012023)));

        when(jdbcTemplate.query(anyString(), any(RowMapper.class),anyInt()))
                .thenReturn(students);

        Student returnVal = repository.findById(7);
        assertEquals(returnVal.getFirstName(),"Kurt");
        assertEquals(returnVal.getLastName(),"Wagner");
    }

    @Test
    public void findByIdTest_fail()
    {
        List<Student> students = new ArrayList<>();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt()))
                .thenReturn(students);
        assertThrows(StudentNotFoundException.class, () -> repository.findById(8));
    }

    @Test
    public void findByNameTest()
    {
        List<Student> students = new ArrayList<>();
        students.add(new Student(8,"Ororo","Monroe",new Date(01012023)));

        when(jdbcTemplate.query(anyString(), any(RowMapper.class),anyString(),anyString()))
                .thenReturn(students);

        Student returnVal = repository.findByStudentName("Ororo","Monroe");
        assertEquals(returnVal.getStudentId(),8);
        assertEquals(returnVal.getGraduationYear(),new Date(01012023));
    }

    @Test
    public void findByNameTest_fail()
    {
        List<Student> students = new ArrayList<>();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class),anyString(),anyString()))
                .thenReturn(students);

        assertThrows(StudentNotFoundException.class, () -> repository.findByStudentName("Shiro","Yashida"));

    }




}
