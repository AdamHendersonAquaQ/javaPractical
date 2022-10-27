package com.aquaq.training.javaPractical.jdbc;

import com.aquaq.training.javaPractical.JavaPracticalApplication;
import com.aquaq.training.javaPractical.classes.Course;
import com.aquaq.training.javaPractical.classes.Enrollment;
import com.aquaq.training.javaPractical.classes.Student;
import com.aquaq.training.javaPractical.errorHandling.CourseEnrollmentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JavaPracticalApplication.class)
public class EnrollmentJdbcDaoTest {
    @Spy
    @InjectMocks
    EnrollmentJdbcDao repository;
    @Mock
    CourseJdbcDao courseRepository;
    @Mock
    StudentJdbcDao studentRepository;
    @Mock
    JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads()
    {
    }

    @Test
    public void findAllTest()
    {
        List<Enrollment> records = List.of(new Enrollment(20,"Jubilation","Lee",5,"History"));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(records);
        List<Enrollment> returnVal = repository.findAll();
        assertEquals(returnVal.size(),1);
        assertEquals(returnVal.get(0).getFirstName(),"Jubilation");
    }

    @Test
    public void findAllTest_fail()
    {
        List<Enrollment> records = new ArrayList<>();
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(records);
        assertThrows(CourseEnrollmentException.class, () -> repository.findAll());
    }

    @Test
    public void findByStudentIdTest()
    {
        List<Enrollment> records = List.of(new Enrollment(23,"Paige","Guthrie",5,"History"));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt()))
                .thenReturn(records);
        List<Enrollment> returnVal = repository.findByStudent(23);
        assertEquals(returnVal.size(),1);
        assertEquals(returnVal.get(0).getFirstName(),"Paige");
    }

    @Test
    public void findByStudentIdTest_fail()
    {
        List<Enrollment> records = new ArrayList<>();
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt()))
                .thenReturn(records);
        assertThrows(CourseEnrollmentException.class, () -> repository.findByStudent(21));
    }

    @Test
    public void findByCourseIdTest()
    {
        List<Enrollment> records = List.of(new Enrollment(24,"Angelo","Espinosa",5,"History"));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt()))
                .thenReturn(records);
        List<Enrollment> returnVal = repository.findByCourse(5);
        assertEquals(returnVal.size(),1);
        assertEquals(returnVal.get(0).getFirstName(),"Angelo");
    }

    @Test
    public void findByCourseIdTest_fail()
    {
        List<Enrollment> records = new ArrayList<>();
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt()))
                .thenReturn(records);
        assertThrows(CourseEnrollmentException.class, () -> repository.findByCourse(21));
    }

    @Test
    public void enrollStudentInCourseTest()
    {
        when(jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenReturn(1);
        assertEquals(repository.enrollStudentInCourse(1,1),
                "Student has been successfully registered");
    }

    @Test
    public void enrollStudentInCourseTest_fail()
    {
        when(jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenReturn(0);
        assertThrows(CourseEnrollmentException.class,
                () -> repository.enrollStudentInCourse(1,1));
    }

    @Test
    public void getStudentCreditsTest()
    {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyString(), anyInt())).thenReturn(5);
        assertEquals(repository.getStudentCredits("WINTER2022",1),5);
    }

    @Test
    public void getStudentCreditsTest_invalidSemesterCodeFail()
    {
        assertThrows(CourseEnrollmentException.class,
                () -> repository.getStudentCredits("failString",1));
    }

    @Test
    public void getStudentCreditsTest_returnsZeroWhenNull()
    {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyString(), anyInt())).thenReturn(null);
        assertEquals(repository.getStudentCredits("WINTER2022",1),0);
    }

    @Test
    public void getCourseCapacityTest()
    {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyInt())).thenReturn(5);
        assertEquals(repository.getCurrentCourseCapacity(1),5);
    }

    @Test
    public void getCourseCapacityTest_returnsZeroWhenNull()
    {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyInt())).thenReturn(null);
        assertEquals(repository.getCurrentCourseCapacity(1),0);
    }

    @Test
    public void checkIfEnrolledTest()
    {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyInt(), anyInt())).thenReturn(1);
        assertTrue(repository.checkIfEnrolled(1,1));
    }

    @Test
    public void checkIfEnrolledTest_false1()
    {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyInt(), anyInt())).thenReturn(null);
        assertFalse(repository.checkIfEnrolled(1,1));
    }

    @Test
    public void checkIfEnrolledTest_false2()
    {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyInt(), anyInt())).thenReturn(0);
        assertFalse(repository.checkIfEnrolled(1,1));
    }

    @Test
    public void enrollStudentTest()
    {
        List<Course> testCourses = List.of(new Course(9,"Biology","Science",
                5,5,"WINTER2023"));
        doReturn(false).when(repository).checkIfEnrolled(anyInt(),anyInt());
        when(studentRepository.findById(anyInt())).thenReturn(List.of(new Student()));
        doReturn(testCourses).when(courseRepository).findById(anyInt());
        doReturn(5).when(repository).getStudentCredits(anyString(),anyInt());
        doReturn(1).when(repository).getCurrentCourseCapacity(anyInt());
        doReturn("Student has been successfully registered").when(repository)
                .enrollStudentInCourse(anyInt(),anyInt());
        assertEquals(repository.enrollStudent(1,1),
                "Student has been successfully registered");
    }

    @Test
    public void enrollStudentTest_fail_alreadyEnrolled()
    {
        doReturn(true).when(repository).checkIfEnrolled(anyInt(),anyInt());
        assertThrows(CourseEnrollmentException.class,()->repository.enrollStudent(1,1));
    }

    @Test
    public void enrollStudentTest_fail_tooManySemesterCredits()
    {
        List<Course> testCourses = List.of(new Course(9,"Biology","Science",
                5,5,"WINTER2023"));
        doReturn(false).when(repository).checkIfEnrolled(anyInt(),anyInt());
        when(studentRepository.findById(anyInt())).thenReturn(List.of(new Student()));
        doReturn(testCourses).when(courseRepository).findById(anyInt());
        doReturn(19).when(repository).getStudentCredits(anyString(),anyInt());
        assertThrows(CourseEnrollmentException.class,()->repository.enrollStudent(1,1));
    }

    @Test
    public void enrollStudentTest_fail_noAvailableSpaces()
    {
        List<Course> testCourses = List.of(new Course(9,"Biology","Science",
                5,5,"WINTER2023"));
        doReturn(false).when(repository).checkIfEnrolled(anyInt(),anyInt());
        when(studentRepository.findById(anyInt())).thenReturn(List.of(new Student()));
        doReturn(testCourses).when(courseRepository).findById(anyInt());
        doReturn(5).when(repository).getStudentCredits(anyString(),anyInt());
        doReturn(10).when(repository).getCurrentCourseCapacity(anyInt());
        assertThrows(CourseEnrollmentException.class,()->repository.enrollStudent(1,1));
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
}
