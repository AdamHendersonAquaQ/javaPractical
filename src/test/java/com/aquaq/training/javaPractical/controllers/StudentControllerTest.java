package com.aquaq.training.javaPractical.controllers;

import com.aquaq.training.javaPractical.classes.Student;
import com.aquaq.training.javaPractical.jdbc.CourseJdbcDao;
import com.aquaq.training.javaPractical.jdbc.EnrollmentJdbcDao;
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
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentJdbcDao studentJdbcDao;
    @MockBean
    private CourseJdbcDao courseJdbcDao;
    @MockBean
    private EnrollmentJdbcDao enrollmentJdbcDao;

    @Test
    public void findAllStudentsTest() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student(9,"Piotr","Rasputin",2025));
        Mockito.when(studentJdbcDao.findAll()).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", Matchers.equalTo("Piotr")));
    }

    @Test
    public void deleteStudentTest() throws Exception {
        Mockito.when(studentJdbcDao.deleteStudent(anyInt()))
                .thenReturn("Student has been successfully deleted.");
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/student/id/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Student has been successfully deleted."));
    }

    @Test
    public void updateStudentTest() throws Exception {
        Student student = new Student(15,"Douglas","Ramsey",2024);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(student);
        Mockito.when(studentJdbcDao.updateStudent(any(Student.class))).thenReturn("Student has been successfully updated. ");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/student/update/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Student has been successfully updated. "));
    }

    @Test
    public void findByIdTest() throws Exception {
        List<Student> students = List.of(new Student(10,"Katherine","Pryde",2025));
        Mockito.when(studentJdbcDao.findById(anyInt())).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/student/id/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", Matchers.equalTo("Katherine")));
    }

    @Test
    public void findByNameTest() throws Exception {
        List<Student> students = List.of(new Student(11,"Alex","Summers",2025));
        Mockito.when(studentJdbcDao.findByStudentName(anyString(),anyString())).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/student/studentName/?firstName=Alex&lastName=Summers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId", Matchers.equalTo(11)));
    }

    @Test
    public void findBySemesterTest() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student(15,"Rahne","Sinclair",2025));
        Mockito.when(studentJdbcDao.findBySemester(anyString())).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/student/semester/AUTUMN2023"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId", Matchers.equalTo(15)));
    }

    @Test
    public void addStudentTest() throws Exception {
        Student outputStudent = new Student(13,"Roberto","DaCosta",2025);
        Mockito.when(studentJdbcDao.addNewStudent(any(Student.class))).thenReturn(outputStudent);

        Student inputStudent = new Student();
        inputStudent.setFirstName("Roberto");
        inputStudent.setFirstName("DaCosta");
        inputStudent.setGraduationYear(2025);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(inputStudent);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/student/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId", Matchers.equalTo(13)))
                .andExpect(jsonPath("$.firstName",Matchers.equalTo("Roberto")));
    }
}
