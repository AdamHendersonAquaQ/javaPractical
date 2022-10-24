package com.aquaq.training.javaPractical.jdbc;

import com.aquaq.training.javaPractical.JavaPracticalApplication;
import com.aquaq.training.javaPractical.classes.Course;
import com.aquaq.training.javaPractical.classes.Student;
import com.aquaq.training.javaPractical.errorHandling.CourseEnrollmentException;
import com.aquaq.training.javaPractical.errorHandling.CourseNotFoundException;
import com.aquaq.training.javaPractical.errorHandling.StudentNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JavaPracticalApplication.class)
public class StudentJdbcDaoTest {

    @InjectMocks
    StudentJdbcDao repository;
    @Mock
    JdbcTemplate jdbcTemplate;
    private final GeneratedKeyHolderFactory keyHolderFactory = mock(GeneratedKeyHolderFactory.class);


    @Test
    void contextLoads()
    {
    }

    @Test
    public void findAllTest()
    {
        List<Student> students = new ArrayList<>();
        students.add(new Student(6,"Sean","Cassidy",2023));
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
    public void deleteStudentTest() {
        when(jdbcTemplate.update(anyString(), anyInt()))
                .thenReturn(1);
        assertEquals(repository.deleteStudent(2),"Student has been successfully deleted.");
    }

    @Test
    public void deleteStudentTest_fail()
    {
        when(jdbcTemplate.update(anyString(), anyInt()))
                .thenReturn(0);
        assertThrows(StudentNotFoundException.class, () -> repository.deleteStudent(1));
    }

    @Test
    public void updateStudentTest()
    {
        Student student = new Student(16,"Rachel","Summers",2034);
        when(jdbcTemplate.update(anyString(),anyString(), anyString(), anyInt(), anyInt())).thenReturn(1);
        assertEquals(repository.updateStudent(student),"Student has been successfully updated. ");
    }

    @Test
    public void updateStudentTest_fail_invalidStudent() {
        Student student = new Student();
        assertThrows(StudentNotFoundException.class, () -> repository.updateStudent(student));
    }

    @Test
    public void updateStudentTest_fail_noStudentFound() {
        Student student = new Student(17,"Elizabeth","Braddock",2024);
        when(jdbcTemplate.update(anyString(),anyString(), anyString(), anyInt(),
                anyInt(), anyString(), anyInt())).thenReturn(0);
        assertThrows(StudentNotFoundException.class, () -> repository.updateStudent(student));
    }
    
    @Test
    public void findByIdTest()
    {
        List<Student> students = new ArrayList<>();
        students.add(new Student(7,"Kurt","Wagner",2023));

        when(jdbcTemplate.query(anyString(), any(RowMapper.class),anyInt()))
                .thenReturn(students);

        Student returnVal = repository.findById(7).get(0);
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
        students.add(new Student(8,"Ororo","Monroe",2023));

        when(jdbcTemplate.query(anyString(), any(RowMapper.class),anyString(),anyString()))
                .thenReturn(students);

        Student returnVal = repository.findByStudentName("Ororo","Monroe").get(0);
        assertEquals(returnVal.getStudentId(),8);
        assertEquals(returnVal.getGraduationYear(),2023);
    }

    @Test
    public void findByNameTest_fail()
    {
        List<Student> students = new ArrayList<>();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class),anyString(),anyString()))
                .thenReturn(students);

        assertThrows(StudentNotFoundException.class, () -> repository.findByStudentName("Shiro","Yashida"));

    }

    @Test
    public void findBySemesterTest()
    {
        List<Student> students = new ArrayList<>();
        students.add(new Student(13,"Samuel","Guthrie",2023));

        when(jdbcTemplate.query(anyString(), any(RowMapper.class),anyString()))
                .thenReturn(students);

        List<Student> returnVal = repository.findBySemester("AUTUMN2022");
        assertEquals(returnVal.get(0).getStudentId(),13);
        assertEquals(returnVal.get(0).getFirstName(),"Samuel");
    }

    @Test
    public void findBySemesterTest_fail_semester_code()
    {
        List<Student> students = new ArrayList<>();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class),anyString()))
                .thenReturn(students);

        assertThrows(StudentNotFoundException.class, () -> repository.findBySemester("test"));
    }

    @Test
    public void findBySemesterTest_fail_no_students()
    {
        List<Student> students = new ArrayList<>();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class),anyString()))
                .thenReturn(students);

        assertThrows(StudentNotFoundException.class, () -> repository.findBySemester("AUTUMN2022"));
    }

    @Test
    public void addStudentTest()
    {
        KeyHolder newKey = new GeneratedKeyHolder(List.of(Map.of("", 14)));
        when(jdbcTemplate.update(anyString(),any(MapSqlParameterSource.class),any(KeyHolder.class))).thenReturn(1);
        when(keyHolderFactory.newKeyHolder()).thenReturn(newKey);
        Student inputStudent = new Student();
        inputStudent.setFirstName("Rahne");
        inputStudent.setLastName("Sinclair");
        inputStudent.setGraduationYear(2025);
        Student outputStudent = repository.addNewStudent(inputStudent);
        assertEquals(outputStudent.getStudentId(),14);
        assertEquals(outputStudent.getFirstName(),inputStudent.getFirstName());
    }

    @Test
    public void addStudentTest_fail()
    {
        Student student = new Student();
        assertThrows(StudentNotFoundException.class, () -> repository.addNewStudent(student));
    }

    @Test
    public void keyHolderFactoryTest()
    {
        GeneratedKeyHolderFactory keyHolderFactory = new GeneratedKeyHolderFactory();
        assertEquals(keyHolderFactory.newKeyHolder().getClass(), GeneratedKeyHolder.class);
    }

    @Test
    public void unEnrollStudentTest()
    {
        when(jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenReturn(1);
        assertEquals("Student has been successfully unenrolled.",repository.unEnrollStudent(1,1));
    }

    @Test
    public void unEnrollStudentTest_fail()
    {
        when(jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenReturn(0);
        assertThrows(CourseEnrollmentException.class,() -> repository.unEnrollStudent(1,1));
    }

    @Test
    public void getCoursesBySemesterTest()
    {
        List<Course> courses = List.of(new Course(1,"Biology",
                "Science",1,1,"WINTER2022"));

        when(jdbcTemplate.query(anyString(),any(RowMapper.class), anyInt(),anyString()))
                .thenReturn(courses);
        List<Course> returnVal = repository.getCoursesBySemester(1,"WINTER2022");
        assertEquals(returnVal.get(0).getCourseName(),"Biology");
    }

    @Test
    public void getCoursesBySemesterTest_fail()
    {
        when(jdbcTemplate.query(anyString(),any(RowMapper.class), anyInt(),anyString()))
                .thenReturn(new ArrayList<Course>());
        assertThrows(CourseNotFoundException.class,
                ()->repository.getCoursesBySemester(1,"WINTER2022"));
    }
}