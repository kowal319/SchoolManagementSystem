package com.example.demo.service.Implementation;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student findById(Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        return studentOptional.orElse(null);
    }

    @Override
    public Student createStudent(Student student) {
        if (student.getFirst_name() == null ) {
            throw new IllegalArgumentException("Field cannot be null");
        }
        if (student.getLast_name().length() > 50 || student.getFirst_name().length() > 50){
            throw new IllegalArgumentException("field cannot be longer than 50 chars");
        }
        if (!student.getFirst_name().matches("^[A-Za-z]+$") || (!student.getLast_name().matches("^[A-Za-z]+$"))) {
            throw new IllegalArgumentException("First Name must conains only letters");
        }
        if(studentRepository.findByEmail(student.getEmail()) != null){
            throw new IllegalArgumentException("Email address must be unique");
        }
        if (student.getEmail() == null || student.getLast_name() == null || student.getPhone_number() == null) {
            throw new NullPointerException("Fiield cannot be null");
        }
        if (!student.getPhone_number().matches("^[0-9]+$")) {
            throw new IllegalArgumentException("Phone number must contain only numbers");
        }
        return studentRepository.save(student);
    }

    @Override
    public String deleteStudent(Long id){
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Student with id: " + id + " not found");
        }

        studentRepository.deleteById(id);
        return "Student with id: " + id + ", deleted";
    }

    @Override
    public Student updateStudent(Long id, Student updatedStudent) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            Student existingStudent = studentOptional.get();
            existingStudent.setFirst_name(updatedStudent.getFirst_name());
            existingStudent.setLast_name(updatedStudent.getLast_name());
            existingStudent.setEmail(updatedStudent.getEmail());
            existingStudent.setPhone_number(updatedStudent.getPhone_number());
            return studentRepository.save(existingStudent);
        } else {
            return null;
        }
    }
}