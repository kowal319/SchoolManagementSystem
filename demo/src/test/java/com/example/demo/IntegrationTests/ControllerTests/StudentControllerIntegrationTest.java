package com.example.demo.IntegrationTests.ControllerTests;

import com.example.demo.IntegrationTests.ServiceTests.TestServiceConfig;
import com.example.demo.SchoolManagementSystemApplication;
import com.example.demo.controller.StudentController;
import com.example.demo.entity.Lesson;
import com.example.demo.entity.Student;
import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.LessonService;
import com.example.demo.service.StudentService;
import com.example.demo.service.SubjectService;
import com.example.demo.service.TeacherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @BeforeEach
    public void setUp() {
        System.out.println("Clearing the database before the test...");
        clearDatabase();
        System.out.println("Database cleared successfully.");
    }

    @Transactional
    public void clearDatabase() {
        // Delete all students from the database
        lessonRepository.deleteAll();
        studentRepository.deleteAll();

        // Flush the changes to the database to ensure they take effect immediately
        lessonRepository.flush();
        studentRepository.flush();
    }

    @Test
    @Transactional
    public void testCreateStudent() throws Exception {
        Student student = new Student("Kylian", "Mbappe", "kylian_mbappe@icloud.com", "555660009");
        // Save the student (if needed)

        mockMvc.perform(MockMvcRequestBuilders.post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.first_name").value("Kylian"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last_name").value("Mbappe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("kylian_mbappe@icloud.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone_number").value("555660009"));
    }


    @Test
    public void testGetAllStudents() throws Exception {
        // Create some students and save them to the database
        Student student1 = new Student("Kylian", "Mbappe", "kylian_mbappe@icloud.com", "555660009");
        Student student2 = new Student("Cristiano", "Ronaldo", "cristiano_ronaldo@icloud.com", "123456789");
        studentService.createStudent(student1);
        studentService.createStudent(student2);

        // Perform the get request to fetch all students
        mockMvc.perform(MockMvcRequestBuilders.get("/students"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].first_name").value("Kylian"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].last_name").value("Mbappe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("kylian_mbappe@icloud.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].phone_number").value("555660009"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].first_name").value("Cristiano"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].last_name").value("Ronaldo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("cristiano_ronaldo@icloud.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].phone_number").value("123456789"));
    }

    @Test
    @Transactional
    public void testDeleteStudent() throws Exception {
        // Create a new student and save it
        Student student = new Student("Kylian", "Mbappe", "kylian_mbappe@icloud.com", "555660009");
        student = studentService.createStudent(student);

        // Perform the delete request
        mockMvc.perform(MockMvcRequestBuilders.delete("/students/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Student with id: " + student.getId() + ", deleted"));

        // Check if the student was deleted from the database
        assertNull(studentService.findById(student.getId()));
    }

    @Test
    public void testGetStudentById() throws Exception {
        // Create a new student and save it
        Student student = new Student("Kylian", "Mbappe", "kylian_mbappe@icloud.com", "555660009");
        student = studentService.createStudent(student);

        // Perform the get request to fetch the student by ID
        mockMvc.perform(MockMvcRequestBuilders.get("/students/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(student.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.first_name").value("Kylian"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last_name").value("Mbappe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("kylian_mbappe@icloud.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone_number").value("555660009"));
    }


    @Test
    @Transactional
    public void testUpdateStudent() throws Exception {
        // Create a new student and save it
        Student student = new Student("Kylian", "Mbappe", "kylian_mbappe@icloud.com", "555660009");
        student = studentService.createStudent(student);

        // Modify the student's information
        student.setFirst_name("Updated Kylian");
        student.setLast_name("Updated Mbappe");
        student.setEmail("updated_kylian_mbappe@icloud.com");
        student.setPhone_number("123456789");

        // Perform the update request
        mockMvc.perform(MockMvcRequestBuilders.put("/students/{id}", student.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(student.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.first_name").value("Updated Kylian"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last_name").value("Updated Mbappe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("updated_kylian_mbappe@icloud.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone_number").value("123456789"));

        // Check if the changes were persisted in the database
        Student updatedStudent = studentService.findById(student.getId());
        assertEquals("Updated Kylian", updatedStudent.getFirst_name());
        assertEquals("Updated Mbappe", updatedStudent.getLast_name());
        assertEquals("updated_kylian_mbappe@icloud.com", updatedStudent.getEmail());
        assertEquals("123456789", updatedStudent.getPhone_number());
    }


    // Helper method to convert objects to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

