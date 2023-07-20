package com.example.demo.IntegrationTests.RespositoryTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.IntegrationTests.ServiceTests.TestServiceConfig;
import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@ContextConfiguration(classes = TestServiceConfig.class)
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @AfterEach
    public void tearDown() {
        studentRepository.deleteAll();
    }

    @Test
    public void testFindByEmailOk() {
        Student student = new Student("Kylian", "Mbappe", "kylian_mbappe@icloud.com", "555660009");
        studentRepository.save(student);

        Student foundStudent = studentRepository.findByEmail("kylian_mbappe@icloud.com");

        assertThat(foundStudent).isNotNull();
        assertEquals("Kylian", foundStudent.getFirst_name());
        assertEquals("Mbappe", foundStudent.getLast_name());
        assertEquals("kylian_mbappe@icloud.com", foundStudent.getEmail());
        assertEquals("555660009", foundStudent.getPhone_number());
    }

    @Test
    public void testFindByEmailNotExisting() {
        Student foundStudent = studentRepository.findByEmail("nonexistent@example.com");

        assertNull(foundStudent);
    }

    @Test
    public void testSaveStudentOk() {
        Student student = new Student("Kylian", "Mbappe", "kylian_mbappe@icloud.com", "555660009");

        Student savedStudent = studentRepository.save(student);

        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getId()).isPositive();
        assertEquals("Kylian", savedStudent.getFirst_name());
        assertEquals("Mbappe", savedStudent.getLast_name());
        assertEquals("kylian_mbappe@icloud.com", savedStudent.getEmail());
        assertEquals("555660009", savedStudent.getPhone_number());
    }

    @Test
    public void testDeleteStudentOk() {
        Student student = new Student("Kylian", "Mbappe", "kylian_mbappe@icloud.com", "555660009");
        student = studentRepository.save(student);

        studentRepository.deleteById(student.getId());

        assertFalse(studentRepository.existsById(student.getId()));
    }

    @Test
    public void testDeleteStudentWithNoExistingId() {
        Long nonExistingId = 9999L;

        assertThrows(EntityNotFoundException.class, () -> {
            studentService.deleteStudent(nonExistingId);
        });
    }

}


