package com.example.demo.IntegrationTests.ServiceTests;

import com.example.demo.entity.Student;
import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = TestServiceConfig.class)
public class StudentServiceIntegrationTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LessonRepository lessonRepository;


    @BeforeEach
    public void setUp() {
        clearDatabase();
    }

    @Transactional
    public void clearDatabase() {
        lessonRepository.deleteAll();
        studentRepository.deleteAll();

        lessonRepository.flush();
        studentRepository.flush();
    }


    @Test
    public void testCreateStudent() {
        // Given
        Student student = new Student("John", "Doe", "john.doe@example.com", "667788990");

        // When
        Student createdStudent = studentService.createStudent(student);

        // Then
        Assertions.assertNotNull(createdStudent.getId());
        Assertions.assertEquals("John", createdStudent.getFirst_name());
        Assertions.assertEquals("Doe", createdStudent.getLast_name());
        Assertions.assertEquals("john.doe@example.com", createdStudent.getEmail());
        Assertions.assertEquals("667788990", createdStudent.getPhone_number());
    }

    @Test
    public void testFindAllStudents() {
        // Given
        Student student1 = new Student("Cezary", "Wilk", "wilk123@gmial.com", "333444034");
        Student student2 = new Student("Pawel", "Domagala", "domagala_pawel@googlemail.com", "112233041");
        studentService.createStudent(student1);
        studentService.createStudent(student2);

        // When
        List<Student> allStudents = studentService.findAllStudents();

        // Then
        Assertions.assertEquals(2, allStudents.size());
    }

    @Test
    public void testFindStudentById() {
        // Given
        Student student = new Student("Cezary", "Wilk", "wilk123@gmial.com", "333444034");
        student = studentService.createStudent(student);

        // When
        Student foundStudent = studentService.findById(student.getId());

        // Then
        Assertions.assertNotNull(foundStudent);
        Assertions.assertEquals(student.getFirst_name(), foundStudent.getFirst_name());
        Assertions.assertEquals(student.getLast_name(), foundStudent.getLast_name());
        Assertions.assertEquals(student.getEmail(), foundStudent.getEmail());
        Assertions.assertEquals(student.getPhone_number(), foundStudent.getPhone_number());
    }

    @Test
    public void testUpdateStudent() {
        // Given
        Student student = new Student("Cezary", "Wilk", "wilk123@gmial.com", "333444034");
        student = studentService.createStudent(student);
        student.setFirst_name("UpdatedFirstName");
        student.setLast_name("UpdatedLastName");
        student.setEmail("updated.email@example.com");
        student.setPhone_number("565656789");

        // When
        Student updatedStudent = studentService.updateStudent(student.getId(), student);

        // Then
        Assertions.assertNotNull(updatedStudent);
        Assertions.assertEquals("UpdatedFirstName", updatedStudent.getFirst_name());
        Assertions.assertEquals("UpdatedLastName", updatedStudent.getLast_name());
        Assertions.assertEquals("updated.email@example.com", updatedStudent.getEmail());
        Assertions.assertEquals("565656789", updatedStudent.getPhone_number());
    }

    @Test
    public void testDeleteStudent() {
        // Given
        Student student = new Student("Cezary", "Wilk", "wilk123@gmial.com", "333444034");
        student = studentService.createStudent(student);

        // When
        String result = studentService.deleteStudent(student.getId());

        // Then
        Assertions.assertEquals("Student with id: "+ student.getId() + ", deleted", result);
        Assertions.assertNull(studentService.findById(student.getId()));
    }
}

