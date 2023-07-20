package com.example.demo.service;

import com.example.demo.entity.Lesson;
import com.example.demo.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;


public interface StudentService {

    List<Student> findAllStudents();
    Student findById(Long id);
    Student createStudent(Student student);
    String deleteStudent(Long id);
    Student updateStudent(Long id, Student updatedStudent);

}
