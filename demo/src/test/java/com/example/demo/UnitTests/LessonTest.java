package com.example.demo.UnitTests;

import com.example.demo.SchoolManagementSystemApplication;
import com.example.demo.entity.Lesson;
import com.example.demo.entity.Student;
import com.example.demo.entity.Subject;
import com.example.demo.entity.Teacher;
import com.example.demo.repository.LessonRepository;
import com.example.demo.service.LessonService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest(classes = SchoolManagementSystemApplication.class)
@Transactional
public class LessonTest {

    @PersistenceContext
    private EntityManager entityManager;

    private Validator validator;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonService lessonService;

    private Subject subject;
    private Teacher teacher;
    private Student student;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
            subject = new Subject("Backend", "Learning java language as backend language for web applications.");
            teacher = new Teacher("Xavii", "Hernandeez", "xavi61243232@Gmail.com", "950467897");
            student = new Student("Pablo", "Gavira", "gavvira01134112@gmail.com", "507606431");

        entityManager.persist(subject);
        entityManager.persist(teacher);
        entityManager.persist(student);
        }

    @Test
    public void testCreateValidLesson() {
        Lesson lesson = new Lesson(subject, teacher, student);

        // When
        Set<ConstraintViolation<Lesson>> violations = validator.validate(lesson);
        Assertions.assertTrue(violations.isEmpty());

        Lesson savedLesson = lessonService.createLesson(lesson);

        // Then
        Assertions.assertNotNull(savedLesson.getId());

        Lesson fetchedLesson = entityManager.find(Lesson.class, savedLesson.getId());
        Assertions.assertEquals(savedLesson, fetchedLesson);
    }

    @Test
    public void testCreateLessonWithNullStudent() {
        // Given
        Lesson lesson = new Lesson(subject, teacher, null);

        // When/Then
        Assertions.assertThrows(NullPointerException.class, () -> lessonService.createLesson(lesson));
    }

    @Test
    public void testCreateLessonWithNullTeacher() {
        // Given
        Lesson lesson = new Lesson(subject, null, student);

        // When/Then
        Assertions.assertThrows(NullPointerException.class, () -> lessonService.createLesson(lesson));
    }

    @Test
    public void testCreateLessonWithNullSubject() {
        // Given
        Lesson lesson = new Lesson(null, teacher, student);

        // When/Then
        Assertions.assertThrows(NullPointerException.class, () -> lessonService.createLesson(lesson));
    }



}
