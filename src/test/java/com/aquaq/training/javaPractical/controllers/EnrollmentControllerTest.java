package com.aquaq.training.javaPractical.controllers;

import com.aquaq.training.javaPractical.classes.Enrollment;
import com.aquaq.training.javaPractical.jdbc.CourseJdbcDao;
import com.aquaq.training.javaPractical.jdbc.EnrollmentJdbcDao;
import com.aquaq.training.javaPractical.jdbc.StudentJdbcDao;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrollmentJdbcDao enrollmentJdbcDao;
    @MockBean
    private CourseJdbcDao courseJdbcDao;
    @MockBean
    private StudentJdbcDao studentJdbcDao;

    @Test
    public void findAllEnrollmentRecordsTest() throws Exception {
        List<Enrollment> records = new ArrayList<>();
        records.add(new Enrollment(19,"Remy","LeBeau",
                5,"History"));
        Mockito.when(enrollmentJdbcDao.findAll()).thenReturn(records);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/enrollment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName", Matchers.equalTo("History")));
    }

    @Test
    public void findAllEnrollmentRecordsByStudentIdTest() throws Exception {
        List<Enrollment> records = new ArrayList<>();
        Enrollment record = new Enrollment();
        record.setStudentId(21);
        record.setFirstName("Monet");
        record.setLastName("St. Croix");
        record.setCourseId(5);
        record.setCourseName("History");
        records.add(record);
        Mockito.when(enrollmentJdbcDao.findByStudent(anyInt())).thenReturn(records);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/enrollment/student/21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName", Matchers.equalTo("History")));
    }

    @Test
    public void findAllEnrollmentRecordsByCourseIdTest() throws Exception {
        List<Enrollment> records = new ArrayList<>();
        records.add(new Enrollment(22,"Jonothan","Starsmore",
                5,"History"));
        Mockito.when(enrollmentJdbcDao.findByCourse(anyInt())).thenReturn(records);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/enrollment/course/22"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName", Matchers.equalTo("History")));
    }

    @Test
    public void enrollStudentTest() throws Exception {
        Mockito.when(enrollmentJdbcDao.enrollStudent(anyInt(), anyInt())).thenReturn("Success");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/enrollment/?courseId=1&studentId=1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }

    @Test
    public void unEnrollStudentTest() throws Exception {
        Mockito.when(enrollmentJdbcDao.unEnrollStudent(anyInt(),anyInt()))
                .thenReturn("Student has been successfully unenrolled.");
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/enrollment/?studentId=1&courseId=1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Student has been successfully unenrolled."));
    }
}
