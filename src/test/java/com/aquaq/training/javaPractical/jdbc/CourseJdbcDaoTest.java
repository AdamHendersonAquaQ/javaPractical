package com.aquaq.training.javaPractical.jdbc;

import com.aquaq.training.javaPractical.JavaPracticalApplication;
import com.aquaq.training.javaPractical.classes.Course;
import com.aquaq.training.javaPractical.errorHandling.CourseEnrollmentException;
import com.aquaq.training.javaPractical.errorHandling.CourseNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JavaPracticalApplication.class)
public class CourseJdbcDaoTest {
    @Spy
    @InjectMocks
    CourseJdbcDao repository;
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
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(9,"Biology","Science",
                5,5,"WINTER2023"));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(courses);

        List<Course> returnVal = repository.findAll();
        assertEquals(returnVal.size(),1);
        assertEquals(returnVal.get(0).getCourseName(),"Biology");
    }

    @Test
    public void findAllTest_fail()
    {
        List<Course> courses = new ArrayList<>();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(courses);

        assertThrows(CourseNotFoundException.class, () -> repository.findAll());
    }

    @Test
    public void deleteCourseTest()
    {
        when(jdbcTemplate.update(anyString(), anyInt()))
                .thenReturn(1);
        assertEquals(repository.deleteCourse(2),"Course has been successfully deleted.");
    }

    @Test
    public void deleteCourseTest_fail()
    {
        when(jdbcTemplate.update(anyString(), anyInt()))
                .thenReturn(0);
        assertThrows(CourseNotFoundException.class, () -> repository.deleteCourse(1));
    }

    @Test
    public void updateCourseTest()
    {
        Course course = new Course(9,"Biology","Science",
                5,5,"WINTER2023");
        when(jdbcTemplate.update(anyString(),anyString(), anyString(), anyInt(),
                anyInt(), anyString(), anyInt())).thenReturn(1);
        assertEquals(repository.updateCourse(course),"Course has been successfully updated. ");
    }

    @Test
    public void updateCourseTest_fail_invalidCourse() {
        Course course = new Course();
        assertThrows(CourseNotFoundException.class, () -> repository.updateCourse(course));
    }

    @Test
    public void updateCourseTest_fail_noCourseFound() {
        Course course = new Course(9,"Biology","Science",
                5,5,"WINTER2023");
        when(jdbcTemplate.update(anyString(),anyString(), anyString(), anyInt(),
                anyInt(), anyString(), anyInt())).thenReturn(0);
        assertThrows(CourseNotFoundException.class, () -> repository.updateCourse(course));
    }

    @Test
    public void findBySemesterTest()
    {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(9,"Biology","Science",
                5,5,"WINTER2023"));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class),anyString()))
                .thenReturn(courses);

        List<Course> returnVal = repository.findBySemester("WINTER2023");
        assertEquals(returnVal.get(0).getCourseName(),"Biology");
    }

    @Test
    public void findBySemesterTest_fail()
    {
        List<Course> courses = new ArrayList<>();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt()))
                .thenReturn(courses);
        assertThrows(CourseNotFoundException.class, () -> repository.findBySemester("WINTER2023"));
    }

    @Test
    public void findByNameTest()
    {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(9,"Biology","Science",
                5,5,"WINTER2023"));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class),anyString()))
                .thenReturn(courses);

        List<Course> returnVal = repository.findByCourseName("Biology");
        assertEquals(returnVal.get(0).getCourseName(),"Biology");
    }

    @Test
    public void findByNameTest_fail()
    {
        List<Course> courses = new ArrayList<>();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt()))
                .thenReturn(courses);
        assertThrows(CourseNotFoundException.class, () -> repository.findByCourseName("Biology"));
    }

    @Test
    public void findByIdTest()
    {
        Course course = new Course(9,"Biology","Science",
                5,5,"WINTER2023");
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class),anyInt()))
                .thenReturn(course);

        Course returnVal = repository.findById(9);
        assertEquals(returnVal.getCourseName(),"Biology");
    }

    @Test
    public void findByIdTest_fail()
    {
        Course course = new Course();

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyInt()))
                .thenReturn(course);
        assertThrows(CourseNotFoundException.class, () -> repository.findById(9));
    }

    @Test
    public void findBySubjectAreaTest()
    {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(9,"Biology","Science",
                5,5,"WINTER2023"));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class),anyString()))
                .thenReturn(courses);

        List<Course> returnVal = repository.findBySubjectArea("Science");
        assertEquals(returnVal.get(0).getCourseName(),"Biology");
    }

    @Test
    public void findBySubjectAreaTest_fail()
    {
        List<Course> courses = new ArrayList<>();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt()))
                .thenReturn(courses);
        assertThrows(CourseNotFoundException.class, () -> repository.findBySubjectArea("WINTER2023"));
    }

    @Test
    public void addCourseTest()
    {
        KeyHolder newKey = new GeneratedKeyHolder(List.of(Map.of("", 14)));
        when(jdbcTemplate.update(anyString(),any(MapSqlParameterSource.class),any(KeyHolder.class))).thenReturn(1);
        when(keyHolderFactory.newKeyHolder()).thenReturn(newKey);
        Course inputCourse = new Course();
        inputCourse.setCourseName("Biology");
        inputCourse.setSubjectArea("Science");
        inputCourse.setCreditAmount(5);
        inputCourse.setStudentCapacity(5);
        inputCourse.setSemesterCode("WINTER2023");
        Course outputCourse = repository.addNewCourse(inputCourse);
        assertEquals(outputCourse.getCourseId(),14);
        assertEquals(outputCourse.getCourseName(),inputCourse.getCourseName());
    }

    @Test
    public void addCourseTest_fail()
    {
        Course course = new Course();
        assertThrows(CourseNotFoundException.class, () -> repository.addNewCourse(course));
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
        Course testCourse = new Course(9,"Biology","Science",
                5,5,"WINTER2023");
        doReturn(false).when(repository).checkIfEnrolled(anyInt(),anyInt());
        doReturn(testCourse).when(repository).findById(anyInt());
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
        Course testCourse = new Course(9,"Biology","Science",
                5,5,"WINTER2023");
        doReturn(false).when(repository).checkIfEnrolled(anyInt(),anyInt());
        doReturn(testCourse).when(repository).findById(anyInt());
        doReturn(19).when(repository).getStudentCredits(anyString(),anyInt());
        assertThrows(CourseEnrollmentException.class,()->repository.enrollStudent(1,1));
    }

    @Test
    public void enrollStudentTest_fail_noAvailableSpaces()
    {
        Course testCourse = new Course(9,"Biology","Science",
                5,5,"WINTER2023");
        doReturn(false).when(repository).checkIfEnrolled(anyInt(),anyInt());
        doReturn(testCourse).when(repository).findById(anyInt());
        doReturn(5).when(repository).getStudentCredits(anyString(),anyInt());
        doReturn(10).when(repository).getCurrentCourseCapacity(anyInt());
        assertThrows(CourseEnrollmentException.class,()->repository.enrollStudent(1,1));
    }

}
