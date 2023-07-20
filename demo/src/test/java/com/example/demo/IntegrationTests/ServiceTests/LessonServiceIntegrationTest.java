package com.example.demo.IntegrationTests.ServiceTests;

import com.example.demo.entity.Lesson;
import com.example.demo.entity.Student;
import com.example.demo.entity.Subject;
import com.example.demo.entity.Teacher;
import com.example.demo.service.LessonService;
import com.example.demo.service.StudentService;
import com.example.demo.service.SubjectService;
import com.example.demo.service.TeacherService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class LessonServiceIntegrationTest {

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
//delete all lessons before each start
        List<Lesson> allLessons = lessonService.findAllLessons();
        for (Lesson lesson : allLessons) {
            lessonService.deleteLesson(lesson.getId());
        }
    }


    @Test
    public void testAddLesson() {
        Subject subject = new Subject("Math", "Mathematics Subject");
        subject = subjectService.createSubject(subject); // Save the subject

        Teacher teacher = new Teacher("Cristiano", "Ronaldo", "cris.crr7@gmail.com", "454546789");
        teacher = teacherService.createTeacher(teacher); // Save the teacher

        Student student = new Student("Kylian", "Mbappe", "kyliianek123@gmail.com", "987004321");
        student = studentService.createStudent(student); // Save the teacher

        Lesson lesson = new Lesson(subject, teacher, student);

        Lesson savedLesson = lessonService.createLesson(lesson);

        assertNotNull(savedLesson.getId());
        assertEquals(subject.getName(), savedLesson.getSubject().getName());
        assertEquals(teacher.getFirst_name(), savedLesson.getTeacher().getFirst_name());
        assertEquals(student.getFirst_name(), savedLesson.getStudent().getFirst_name());
    }

    @Test
    public void testDeleteLesson() {
        Subject subject = new Subject("Physics", "Physics Subject");
        subject = subjectService.createSubject(subject);
        Teacher teacher = new Teacher("Albert", "Einstein", "albert.einstein@example.com", "545454545");
        teacher = teacherService.createTeacher(teacher);
        Student student = new Student("Marie", "Curie", "marie.curie@example.com", "989898989");
        student = studentService.createStudent(student);

        Lesson lesson = new Lesson(subject, teacher, student);
        lesson = lessonService.createLesson(lesson);

        Long lessonId = lesson.getId();
        assertNotNull(lessonId);

        // Delete the lesson
        String result = lessonService.deleteLesson(lessonId);
        assertEquals("Lesson with id: " + lessonId + ", deleted", result);

        // Check that the lesson no longer exists in the database
        Lesson deletedLesson = lessonService.findById(lessonId);
        assertNull(deletedLesson);
    }

    @Test
    public void testFindLessonById() {
        Subject subject = new Subject("Chemistry", "Chemistry Subject");
        subject = subjectService.createSubject(subject);
        Teacher teacher = new Teacher("Marie", "Curie", "marie.curie@example.com", "989898989");
        teacher = teacherService.createTeacher(teacher);
        Student student = new Student("Isaac", "Newton", "isaac.newton@example.com", "545454545");
        student = studentService.createStudent(student);

        Lesson lesson = new Lesson(subject, teacher, student);
        lesson = lessonService.createLesson(lesson);

        Long lessonId = lesson.getId();
        assertNotNull(lessonId);

        Lesson foundLesson = lessonService.findById(lessonId);

        assertNotNull(foundLesson);
        assertEquals(subject.getName(), foundLesson.getSubject().getName());
        assertEquals(teacher.getFirst_name(), foundLesson.getTeacher().getFirst_name());
        assertEquals(student.getFirst_name(), foundLesson.getStudent().getFirst_name());
    }
    @Test
    public void testUpdateLesson() {
        Subject subject = new Subject("Biology", "Biology Subject");
        subject = subjectService.createSubject(subject);
        Teacher teacher = new Teacher("Charles", "Darwin", "charles.darwin@example.com", "989898989");
        teacher = teacherService.createTeacher(teacher);
        Student student = new Student("Nikola", "Tesla", "nikola.tesla@example.com", "545454545");
        student = studentService.createStudent(student);

        Lesson lesson = new Lesson(subject, teacher, student);
        lesson = lessonService.createLesson(lesson);

        Long lessonId = lesson.getId();
        assertNotNull(lessonId);

        Subject updatedSubject = new Subject("Updated Biology", "Updated Biology Subject");
        Teacher updatedTeacher = new Teacher("Updated Charles", "Darwin", "updated.charles.darwin@example.com", "111111111");
        Student updatedStudent = new Student("Updated Nikola", "Tesla", "updated.nikola.tesla@example.com", "222222222");

        lesson.setSubject(updatedSubject);
        lesson.setTeacher(updatedTeacher);
        lesson.setStudent(updatedStudent);

        Lesson result = lessonService.updateLesson(lessonId, lesson);

        assertNotNull(result);
        assertEquals("Updated Biology", result.getSubject().getName());
        assertEquals("Updated Charles", result.getTeacher().getFirst_name());
        assertEquals("Updated Nikola", result.getStudent().getFirst_name());
    }

    @Test
    public void testFindAllLessons() {
        Subject subject1 = new Subject("Math", "Mathematics Subject");
        Subject subject2 = new Subject("Physics", "Physics Subject");
        subject1 = subjectService.createSubject(subject1);
        subject2 = subjectService.createSubject(subject2);

        Teacher teacher1 = new Teacher("Adam", "Mickiewicz", "micki@gmail.com", "989898989");
        Teacher teacher2 = new Teacher("Peter", "Parker", "spider_web@gmail.com", "545454545");
        teacher1 = teacherService.createTeacher(teacher1);
        teacher2 = teacherService.createTeacher(teacher2);

        Student student1 = new Student("Dawid", "Kownacki", "kowas@wp.com", "111111111");
        Student student2 = new Student("Bob", "Sinclar", "bob.sinclar@onet.com", "222222222");
        student1 = studentService.createStudent(student1);
        student2 = studentService.createStudent(student2);

        Lesson lesson1 = new Lesson(subject1, teacher1, student1);
        Lesson lesson2 = new Lesson(subject2, teacher2, student2);
        lesson1 = lessonService.createLesson(lesson1);
        lesson2 = lessonService.createLesson(lesson2);

        List<Lesson> allLessons = lessonService.findAllLessons();

        assertNotNull(allLessons);
        assertEquals(2, allLessons.size());
        assertTrue(allLessons.contains(lesson1));
        assertTrue(allLessons.contains(lesson2));
    }



}

