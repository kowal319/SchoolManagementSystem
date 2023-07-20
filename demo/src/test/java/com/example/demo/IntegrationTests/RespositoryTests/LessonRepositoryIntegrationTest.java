package com.example.demo.IntegrationTests.RespositoryTests;

import com.example.demo.IntegrationTests.ServiceTests.TestServiceConfig;
import com.example.demo.entity.Lesson;
import com.example.demo.entity.Student;
import com.example.demo.entity.Subject;
import com.example.demo.entity.Teacher;
import com.example.demo.repository.LessonRepository;
import com.example.demo.service.LessonService;
import com.example.demo.service.StudentService;
import com.example.demo.service.SubjectService;
import com.example.demo.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestServiceConfig.class)
public class LessonRepositoryIntegrationTest {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonService lessonService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        List<Lesson> allLessons = lessonService.findAllLessons();
        for (Lesson lesson : allLessons) {
            lessonService.deleteLesson(lesson.getId());
        }
    }

    @Test
    @Transactional
    public void testSaveLesson() {
        Subject subject = new Subject("Math", "Mathematics Subject");
        subject = subjectService.createSubject(subject); // Save the subject

        Teacher teacher = new Teacher("Cristiano","Ronaldo", "cris.cr7@gmail.com", "123456789");
        teacher = teacherService.createTeacher(teacher); // Save the subject

        Student student = new Student("Kylian", "Mbappe", "kylianek123@gmail.com", "987654321");
        student = studentService.createStudent(student); // Save the subject

        Lesson lesson = new Lesson(subject, teacher, student);

        Lesson savedLesson = lessonRepository.save(lesson);

        assertNotNull(savedLesson.getId());
        assertEquals(subject.getName(), savedLesson.getSubject().getName());
        assertEquals(teacher.getFirst_name(), savedLesson.getTeacher().getFirst_name());
        assertEquals(student.getFirst_name(), savedLesson.getStudent().getFirst_name());
    }

    @Test
    public void testFindAll() {
        Subject subject = new Subject("Math", "Mathematics Subject");
        subject = subjectService.createSubject(subject);

        Teacher teacher = new Teacher("Cristiano", "Ronaldo", "cris.cr7@gmail.com", "123456789");
        teacher = teacherService.createTeacher(teacher);

        Student student = new Student("Kylian", "Mbappe", "kylianek123@gmail.com", "987654321");
        student = studentService.createStudent(student);

        Lesson lesson1 = new Lesson(subject, teacher, student);
        Lesson lesson2 = new Lesson(subject, teacher, student);

        lessonRepository.save(lesson1);
        lessonRepository.save(lesson2);

        List<Lesson> allLessons = lessonRepository.findAll();

        assertEquals(2, allLessons.size());
    }
}

