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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseJdbcDao courseJdbcDao;
    @MockBean
    private StudentJdbcDao studentJdbcDao;

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
}
