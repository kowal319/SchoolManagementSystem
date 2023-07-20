package com.example.demo.service;

import com.example.demo.entity.Lesson;
import com.example.demo.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;


public interface LessonService {

    List<Lesson> findAllLessons();
    Lesson createLesson(Lesson lesson);
    String deleteLesson(Long id);
    Lesson findById(Long id);
    Lesson updateLesson(Long id, Lesson updatedLesson);


}
