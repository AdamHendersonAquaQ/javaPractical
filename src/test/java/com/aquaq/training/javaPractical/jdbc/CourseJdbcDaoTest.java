package com.aquaq.training.javaPractical.jdbc;

import com.aquaq.training.javaPractical.JavaPracticalApplication;
import com.aquaq.training.javaPractical.classes.Course;
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
        when(jdbcTemplate.queryForObject(anyString(),eq(Integer.class),anyInt())).thenReturn(0);
        when(jdbcTemplate.query(anyString(),any(RowMapper.class),anyString(), anyString())).thenReturn(List.of(course));
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
        when(jdbcTemplate.queryForObject(anyString(),eq(Integer.class),anyInt())).thenReturn(0);
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
        List<Course> courses = List.of(new Course(9,"Biology","Science",
                5,5,"WINTER2023"));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class),anyInt()))
                .thenReturn(courses);

        List<Course> returnVal = repository.findById(9);
        assertEquals(returnVal.get(0).getCourseName(),"Biology");
    }

    @Test
    public void findByIdTest_fail()
    {
        List<Course> courses = new ArrayList<>();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt()))
                .thenReturn(courses);
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
        when(jdbcTemplate.query(anyString(),any(RowMapper.class),anyString(), anyString())).thenReturn(new ArrayList<Course>());
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
    public void addCourseTest_fail_noName()
    {
        Course course = new Course();
        assertThrows(CourseNotFoundException.class, () -> repository.addNewCourse(course));
    }

    @Test
    public void addCourseTest_fail_badName()
    {
        Course course = new Course(0,"B&*^^%(","English",5,5,"WINTER2023");
        assertThrows(CourseNotFoundException.class, () -> repository.addNewCourse(course));
    }

    @Test
    public void addCourseTest_fail_courseExists()
    {
        Course course = new Course(0,"Biology","English",5,5,"WINTER2023");
        Course retCourse = new Course(4,"Biology","English",5,5,"WINTER2023");
        when(jdbcTemplate.query(anyString(),any(RowMapper.class),anyString(), anyString())).thenReturn(List.of(retCourse));
        assertThrows(CourseNotFoundException.class, () -> repository.addNewCourse(course));
    }

    @Test
    public void addCourseTest_fail_badSemesterCode()
    {
        Course course = new Course(0,"Biology","English",5,5,"Summer?");
        assertThrows(CourseNotFoundException.class, () -> repository.addNewCourse(course));
    }

    @Test
    public void addCourseTest_fail_creditAmountLessThanZero()
    {
        KeyHolder newKey = new GeneratedKeyHolder(List.of(Map.of("", 14)));
        when(jdbcTemplate.update(anyString(),any(MapSqlParameterSource.class),any(KeyHolder.class))).thenReturn(1);
        when(keyHolderFactory.newKeyHolder()).thenReturn(newKey);
        Course inputCourse = new Course(0,"Biology","Science",-1,10,"WINTER2023");
        assertThrows(CourseNotFoundException.class, () -> repository.addNewCourse(inputCourse));
    }

    @Test
    public void addCourseTest_fail_creditAmountGreaterThan20()
    {
        KeyHolder newKey = new GeneratedKeyHolder(List.of(Map.of("", 14)));
        when(jdbcTemplate.update(anyString(),any(MapSqlParameterSource.class),any(KeyHolder.class))).thenReturn(1);
        when(keyHolderFactory.newKeyHolder()).thenReturn(newKey);
        Course inputCourse = new Course(0,"Biology","Science",21,10,"WINTER2023");
        assertThrows(CourseNotFoundException.class, () -> repository.addNewCourse(inputCourse));
    }

    @Test
    public void addCourseTest_fail_studentCapacityLessThanZero()
    {
        KeyHolder newKey = new GeneratedKeyHolder(List.of(Map.of("", 14)));
        when(jdbcTemplate.update(anyString(),any(MapSqlParameterSource.class),any(KeyHolder.class))).thenReturn(1);
        when(keyHolderFactory.newKeyHolder()).thenReturn(newKey);
        Course inputCourse = new Course(0,"Biology","Science",5,-5,"WINTER2023");
        assertThrows(CourseNotFoundException.class, () -> repository.addNewCourse(inputCourse));
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

    @Test
    public void getCapacityTest()
    {
        List<Map<String, Object>> result = List.of(Map.of("1",1));
        when(jdbcTemplate.queryForList(anyString())).thenReturn(result);
        List<Map<String, Object>> returnVal = repository.getCapacity();
        assertEquals(returnVal.get(0).get("1"),1);
    }

}
