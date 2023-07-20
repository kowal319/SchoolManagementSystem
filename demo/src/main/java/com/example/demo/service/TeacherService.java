package com.example.demo.service;

import com.example.demo.entity.Teacher;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TeacherService {


    List<Teacher> findAllTeachers();
    Teacher findById(Long id);
    Teacher createTeacher(Teacher teacher);
    String deleteTeacher(Long id);
    Teacher updateTeacher(Long id, Teacher updatedTeacher);
}
