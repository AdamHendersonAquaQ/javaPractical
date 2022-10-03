package com.aquaq.training.javaPractical;

import com.aquaq.training.javaPractical.jdbc.StudentJdbcDao;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentJdbcDao studentJdbcDao;

    @Test
    public void findAllStudentsTest() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student(9,"Piotr","Rasputin",new Date(01012025)));
        Mockito.when(studentJdbcDao.findAll()).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", Matchers.equalTo("Piotr")));
    }

    @Test
    public void findByIdTest() throws Exception {
        Student student = new Student(10,"Katherine","Pryde",new Date(01012025));
        Mockito.when(studentJdbcDao.findById(anyInt())).thenReturn(student);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/student/id/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", Matchers.equalTo("Katherine")));
    }

    @Test
    public void findByNameTest() throws Exception {
        Student student = new Student(11,"Alex","Summers",new Date(01012025));
        Mockito.when(studentJdbcDao.findByStudentName(anyString(),anyString())).thenReturn(student);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/student/studentName/?firstName=Alex&lastName=Summers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId", Matchers.equalTo(11)));
    }
}
