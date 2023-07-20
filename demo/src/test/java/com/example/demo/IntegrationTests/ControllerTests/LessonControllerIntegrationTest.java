package com.example.demo.IntegrationTests.ControllerTests;

import com.example.demo.entity.Lesson;
import com.example.demo.entity.Student;
import com.example.demo.entity.Subject;
import com.example.demo.entity.Teacher;
import com.example.demo.service.LessonService;
import com.example.demo.service.StudentService;
import com.example.demo.service.SubjectService;
import com.example.demo.service.TeacherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class LessonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SubjectService subjectService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private LessonService lessonService;

    @Test
    @Transactional
    public void testAddLesson() throws Exception {
        Subject subject = new Subject("Math", "Mathematics Subject");
        subject = subjectService.createSubject(subject);

        Teacher teacher = new Teacher("Cristiano","Ronaldo", "cris.cr07@gmail.com", "666655567");
        teacher = teacherService.createTeacher(teacher);

        Student student = new Student("Kylian", "Mbappe", "kylian456@gmail.com", "555667789");
        student = studentService.createStudent(student);

        Lesson lesson = new Lesson(subject, teacher, student);

        mockMvc.perform(post("/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lesson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.subject.name").value("Math"))
                .andExpect(jsonPath("$.teacher.first_name", equalTo("Cristiano")))
                .andExpect(jsonPath("$.student.first_name").value("Kylian"));
    }

    @Test
    public void testGetAllLessons() throws Exception {
        mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetLessonById() throws Exception {
        Long lessonId = 1L;

        mockMvc.perform(get("/lessons/{id}", lessonId))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testUpdateLesson() throws Exception {
        Subject subject = new Subject("Math", "Mathematics Subject");
        subject = subjectService.createSubject(subject);

        Teacher teacher = new Teacher("Cristiano", "Ronaldo", "cris.cr07@gmail.com", "666655567");
        teacher = teacherService.createTeacher(teacher);

        Student student = new Student("Kylian", "Mbappe", "kylian456@gmail.com", "555667789");
        student = studentService.createStudent(student);

        Lesson lesson = new Lesson(subject, teacher, student);
        byte[] lessonJson = objectMapper.writeValueAsBytes(lesson);

        mockMvc.perform(post("/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        List<Lesson> allLessons = lessonService.findAllLessons();
        Lesson createdLesson = allLessons.get(0);

        Subject updatedSubject = new Subject("Updated Math", "Updated Mathematics Subject");
        Teacher updatedTeacher = new Teacher("Updated Cristiano", "Updated Ronaldo", "updated.cris.cr07@gmail.com", "777778888");
        Student updatedStudent = new Student("Updated Kylian", "Updated Mbappe", "updated.kylian456@gmail.com", "999991111");

        createdLesson.setSubject(updatedSubject);
        createdLesson.setTeacher(updatedTeacher);
        createdLesson.setStudent(updatedStudent);

        byte[] updatedLessonJson = objectMapper.writeValueAsBytes(createdLesson);

        mockMvc.perform(put("/lessons/{id}", createdLesson.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedLessonJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(createdLesson.getId().intValue())))
                .andExpect(jsonPath("$.subject.name", equalTo("Updated Math")))
                .andExpect(jsonPath("$.subject.description", equalTo("Updated Mathematics Subject")))
                .andExpect(jsonPath("$.teacher.first_name", equalTo("Updated Cristiano")))
                .andExpect(jsonPath("$.teacher.last_name", equalTo("Updated Ronaldo")))
                .andExpect(jsonPath("$.teacher.email", equalTo("updated.cris.cr07@gmail.com")))
                .andExpect(jsonPath("$.teacher.phone_number", equalTo("777778888")))
                .andExpect(jsonPath("$.student.first_name", equalTo("Updated Kylian")))
                .andExpect(jsonPath("$.student.last_name", equalTo("Updated Mbappe")))
                .andExpect(jsonPath("$.student.email", equalTo("updated.kylian456@gmail.com")))
                .andExpect(jsonPath("$.student.phone_number", equalTo("999991111")));
    }

    @Test
    @Transactional
    public void testDeleteLesson() throws Exception {
        Subject subject = new Subject("Math", "Mathematics Subject");
        subject = subjectService.createSubject(subject);

        Teacher teacher = new Teacher("Cristiano", "Ronaldo", "cris.cr07@gmail.com", "666655567");
        teacher = teacherService.createTeacher(teacher);

        Student student = new Student("Kylian", "Mbappe", "kylian456@gmail.com", "555667789");
        student = studentService.createStudent(student);

        Lesson lesson = new Lesson(subject, teacher, student);
        byte[] lessonJson = objectMapper.writeValueAsBytes(lesson);

        mockMvc.perform(post("/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        List<Lesson> allLessons = lessonService.findAllLessons();
        Lesson createdLesson = allLessons.get(0);

        mockMvc.perform(delete("/lessons/{id}", createdLesson.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Lesson with id: " + createdLesson.getId() + ", deleted"));

        mockMvc.perform(get("/lessons/{id}", createdLesson.getId()))
                .andExpect(status().isNotFound());
    }
}
