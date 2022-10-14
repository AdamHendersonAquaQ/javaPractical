package com.aquaq.training.javaPractical.controllers;

import com.aquaq.training.javaPractical.classes.Course;
import com.aquaq.training.javaPractical.jdbc.CourseJdbcDao;
import com.aquaq.training.javaPractical.jdbc.StudentJdbcDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseJdbcDao courseJdbcDao;
    @MockBean
    private StudentJdbcDao studentJdbcDao;

    @Test
    public void findAllCoursesTest() throws Exception {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(9,"Biology","Science",
                5,5,"WINTER2023"));
        Mockito.when(courseJdbcDao.findAll()).thenReturn(courses);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/course"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName", Matchers.equalTo("Biology")));
    }

    @Test
    public void deleteCourseTest() throws Exception {
        Mockito.when(courseJdbcDao.deleteCourse(anyInt()))
                .thenReturn("Course has been successfully deleted.");
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/course/id/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Course has been successfully deleted."));
    }

    @Test
    public void updateCourseTest() throws Exception {
        Course course = new Course(9,"Biology","Science",
                5,5,"WINTER2023");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(course);
        Mockito.when(courseJdbcDao.updateCourse(any(Course.class))).thenReturn("Course has been successfully updated. ");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/course/update/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Course has been successfully updated. "));

    }

    @Test
    public void findBySemesterTest() throws Exception {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(9,"Biology","Science",
                5,5,"WINTER2023"));
        Mockito.when(courseJdbcDao.findBySemester(anyString())).thenReturn(courses);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/course/semester/WINTER2023"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName", Matchers.equalTo("Biology")));
    }

    @Test
    public void findByNameTest() throws Exception {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(9,"Biology","Science",
                5,5,"WINTER2023"));
        Mockito.when(courseJdbcDao.findByCourseName(anyString())).thenReturn(courses);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/course/name/Biology"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName", Matchers.equalTo("Biology")));
    }

    @Test
    public void findByIdTest() throws Exception {
        Course course = new Course(9,"Biology","Science",
                5,5,"WINTER2023");
        Mockito.when(courseJdbcDao.findById(anyInt())).thenReturn(course);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/course/id/9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseName", Matchers.equalTo("Biology")));
    }

    @Test
    public void findBySubjectAreaTest() throws Exception {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(9,"Biology","Science",
                5,5,"WINTER2023"));
        Mockito.when(courseJdbcDao.findBySubjectArea(anyString())).thenReturn(courses);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/course/subject/Science"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName", Matchers.equalTo("Biology")));
    }
    
    @Test
    public void addCourseTest() throws Exception {
        Course outputCourse = new Course(13,"Biology",
                "Science",5,5,"WINTER2023");
        Mockito.when(courseJdbcDao.addNewCourse(any(Course.class))).thenReturn(outputCourse);

        Course inputCourse = new Course();
        inputCourse.setCourseName("Biology");
        inputCourse.setSubjectArea("Science");
        inputCourse.setCreditAmount(5);
        inputCourse.setStudentCapacity(5);
        inputCourse.setSemesterCode("WINTER2023");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(inputCourse);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/course/addCourse/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId", Matchers.equalTo(13)))
                .andExpect(jsonPath("$.courseName",Matchers.equalTo("Biology")));
    }

    @Test
    public void enrollStudentTest() throws Exception {
        Mockito.when(courseJdbcDao.enrollStudent(anyInt(), anyInt())).thenReturn("Success");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/course/enrollStudent/?courseId=1&studentId=1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }
}
