package com.aquaq.training.javaPractical.jdbc;

import com.aquaq.training.javaPractical.JavaPracticalApplication;
import com.aquaq.training.javaPractical.classes.Course;
import com.aquaq.training.javaPractical.errorHandling.CourseNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
