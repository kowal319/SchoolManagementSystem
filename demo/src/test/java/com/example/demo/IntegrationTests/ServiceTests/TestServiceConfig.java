package com.example.demo.IntegrationTests.ServiceTests;

import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.service.Implementation.LessonServiceImpl;
import com.example.demo.service.Implementation.StudentServiceImpl;
import com.example.demo.service.Implementation.SubjectServiceImpl;
import com.example.demo.service.Implementation.TeacherServiceImpl;
import com.example.demo.service.LessonService;
import com.example.demo.service.StudentService;
import com.example.demo.service.SubjectService;
import com.example.demo.service.TeacherService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestServiceConfig {

    @Bean
    public LessonService lessonService(LessonRepository lessonRepository) {
        return new LessonServiceImpl(lessonRepository);
    }
    @Bean
    public SubjectService subjectService(SubjectRepository subjectRepository) {
        return new SubjectServiceImpl(subjectRepository);
    }
    @Bean
    public TeacherService teacherService(TeacherRepository teacherRepository){
        return new TeacherServiceImpl(teacherRepository);
    }
    @Bean
    public StudentService studentService(StudentRepository studentRepository){
        return new StudentServiceImpl(studentRepository);
    }

}