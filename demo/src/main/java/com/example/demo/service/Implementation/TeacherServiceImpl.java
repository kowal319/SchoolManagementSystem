package com.example.demo.service.Implementation;

import com.example.demo.entity.Teacher;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.service.TeacherService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public List<Teacher> findAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher findById(Long id) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);
        return teacherOptional.orElse(null);
    }

    @Override
    public Teacher createTeacher(Teacher teacher) {
        if (teacher.getFirst_name() == null ) {
            throw new IllegalArgumentException("Field cannot be null");
        }
        if (teacher.getLast_name().length() > 50 || teacher.getFirst_name().length() > 50){
            throw new IllegalArgumentException("field cannot be longer than 50 chars");
        }
        if (!teacher.getFirst_name().matches("^[A-Za-z]+$") || (!teacher.getLast_name().matches("^[A-Za-z]+$"))) {
            throw new IllegalArgumentException("First Name must contains only letters");
        }
        if(teacherRepository.findByEmail(teacher.getEmail()) != null){
            throw new IllegalArgumentException("Email address must be unique");
        }
        if (teacher.getEmail() == null || teacher.getLast_name() == null || teacher.getPhone_number() == null) {
            throw new NullPointerException("Fiield cannot be null");
        }
        if (!teacher.getPhone_number().matches("^[0-9]+$")) {
            throw new IllegalArgumentException("Phone number must contain only numbers");
        }
        return teacherRepository.save(teacher);
    }

    @Override
    public String deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
        return "Teacher with id: " + id + ", deleted";
    }

    @Override
    public Teacher updateTeacher(Long id, Teacher updatedTeacher) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);
        if (teacherOptional.isPresent()) {
            Teacher existingTeacher = teacherOptional.get();
            existingTeacher.setFirst_name(updatedTeacher.getFirst_name());
            existingTeacher.setLast_name(updatedTeacher.getLast_name());
            existingTeacher.setEmail(updatedTeacher.getEmail());
            existingTeacher.setPhone_number(updatedTeacher.getPhone_number());
            return teacherRepository.save(existingTeacher);
        } else {
            return null;
        }
    }
}