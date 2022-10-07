package com.aquaq.training.javaPractical.jdbc;

import com.aquaq.training.javaPractical.JavaPracticalApplication;
import com.aquaq.training.javaPractical.classes.Course;
import com.aquaq.training.javaPractical.classes.Student;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JavaPracticalApplication.class)
public class CourseJdbcDaoTest {
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
}
