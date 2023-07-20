package com.example.demo.controller;

import com.example.demo.entity.Lesson;
import com.example.demo.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;

    @Autowired
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    public Lesson createLesson(@RequestBody Lesson lesson){
        return lessonService.createLesson(lesson);
    }

    @GetMapping
    public List<Lesson> getAllLesons(){
        return lessonService.findAllLessons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable Long id){
        Lesson lesson = lessonService.findById(id);
        if(lesson != null){
            return ResponseEntity.ok(lesson);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLesson(@PathVariable Long id){
        String result = lessonService.deleteLesson(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lesson> updateLesson(@PathVariable Long id, @RequestBody Lesson updatedLesson){
        Lesson updatedEntity = lessonService.updateLesson(id, updatedLesson);
        if(updatedEntity != null){
            return ResponseEntity.ok(updatedEntity);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
