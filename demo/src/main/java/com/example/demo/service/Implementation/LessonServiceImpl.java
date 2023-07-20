package com.example.demo.service.Implementation;

import com.example.demo.entity.Lesson;
import com.example.demo.repository.LessonRepository;
import com.example.demo.service.LessonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public List<Lesson> findAllLessons() {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson createLesson(Lesson lesson) {
        if(lesson.getSubject() == null || lesson.getTeacher() == null || lesson.getStudent() == null){
            throw new NullPointerException("Field cannot be null");
        }
        return lessonRepository.save(lesson);
    }
    @Override
    public String deleteLesson(Long id) {
        lessonRepository.deleteById(id);
        return "Lesson with id: " + id + ", deleted";
    }

    @Override
    public Lesson findById(Long id) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(id);
        return lessonOptional.orElse(null);
    }

    @Override
    public Lesson updateLesson(Long id, Lesson updatedLesson) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(id);
        if (lessonOptional.isPresent()) {
            Lesson existingLesson = lessonOptional.get();
            existingLesson.setStudent(updatedLesson.getStudent());
            existingLesson.setTeacher(existingLesson.getTeacher());
            existingLesson.setSubject(existingLesson.getSubject());
            return lessonRepository.save(existingLesson);
        } else {
            return null;
        }
    }
}
