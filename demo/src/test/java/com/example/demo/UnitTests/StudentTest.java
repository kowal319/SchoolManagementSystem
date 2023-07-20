package com.example.demo.UnitTests;

import com.example.demo.SchoolManagementSystemApplication;
import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.Implementation.StudentServiceImpl;
import com.example.demo.service.StudentService;
import jakarta.transaction.Transactional;
import jakarta.validation.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = SchoolManagementSystemApplication.class)
@Transactional
public class StudentTest {

    @Test
    void contextLoads() {
    }

private StudentRepository studentRepository;
    private StudentService studentService;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        studentRepository = Mockito.mock(StudentRepository.class);
        studentService = new StudentServiceImpl(studentRepository);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testFindAllStudents() {
        // Given
        List<Student> expectedStudents = new ArrayList<>();
        expectedStudents.add(new Student("Leo", "Messi", "messi123@gmail.com", "567654567"));
        expectedStudents.add(new Student("Erling", "Halland", "erling.halland@city.com", "765678999"));
        when(studentRepository.findAll()).thenReturn(expectedStudents);

        // When
        List<Student> actualStudents = studentService.findAllStudents();

        // Then
        Assertions.assertEquals(expectedStudents.size(), actualStudents.size());
        Assertions.assertEquals(expectedStudents, actualStudents);
    }

    @Test
    public void testCreateStudent() {
        // Given
        Student student = new Student("Leo", "Messi", "messi123@gmail.com", "567654567");
        when(studentRepository.save(Mockito.any(Student.class))).thenReturn(student);

        // When
        Student savedStudent = studentService.createStudent(student);

        // Then
        Assertions.assertEquals(student, savedStudent);
    }

    @Test
    public void testDeleteStudent() {
        // Given
        Long studentId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(java.util.Optional.of(new Student("Leo", "Messi", "messi123@gmail.com", "567654567")));
        Mockito.doNothing().when(studentRepository).deleteById(studentId);

        // When
        String result = studentService.deleteStudent(studentId);

        // Then
        Assertions.assertEquals("Student with id: " + studentId + ", deleted", result);
    }

    @Test
    public void testCreateStudentWithNullName() {
        // Given
        Student student = new Student(null, "Messi", "messi123@gmail.com", "567654567");

        assertThrows(IllegalArgumentException.class, () -> studentService.createStudent(student));
    }
    @Test
    public void testCreateStudentWithNullLastName() {
        // Given
        Student student = new Student("Leo", null, "messi123@gmail.com", "567654567");

        assertThrows(NullPointerException.class, () -> studentService.createStudent(student));
    }
    @Test
    public void testCreateStudentWithNullPhone() {
        // Given
        Student student = new Student("Leo", "Messi", "messi123@gmail.com", null);

        assertThrows(NullPointerException.class, () -> studentService.createStudent(student));
    }
    @Test
    public void testCreateStudentWithNullEmail() {
        Student student = new Student("Leo", "Messi", null, "345323432");

        assertThrows(NullPointerException.class, () -> studentService.createStudent(student));

    }
    @Test
    public  void testInvalidEmail(){
        //Given
        String invalidEmail = "invalid.email";
        Student student  = new Student();
        student.setEmail(invalidEmail);

        //When/Then
        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        ConstraintViolation<Student> violation = violations.iterator().next();
        Assertions.assertEquals("email", violation.getPropertyPath().toString());
        Assertions.assertEquals("Invalid email address format", violation.getMessage());
    }

    @Test
    public void testInvalidLastName() {
        // Given
        String invalidLastName = "123"; // Invalid last name containing digits
        Student student = new Student();
        student.setLast_name(invalidLastName);

        // When/Then
        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        ConstraintViolation<Student> violation = violations.iterator().next();
        Assertions.assertEquals("last_name", violation.getPropertyPath().toString());
        Assertions.assertEquals("Last name should contain only letters", violation.getMessage());
    }

    @Test
    public void testInvalidFirstName() {
        // Given
        String invalidFirstName = "John123"; // Invalid first name containing digits
        Student student = new Student();
        student.setFirst_name(invalidFirstName);

        // When/Then
        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        ConstraintViolation<Student> violation = violations.iterator().next();
        Assertions.assertEquals("first_name", violation.getPropertyPath().toString());
        Assertions.assertEquals("First name should contain only letters", violation.getMessage());
    }

    @Test
    public void testInvalidPhoneNumberLengthToShort() {
        // Given
        String invalidPhoneNumber = "123";
        Student student = new Student();
        student.setPhone_number(invalidPhoneNumber);

        // When/Then
        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        ConstraintViolation<Student> violation = violations.iterator().next();
        Assertions.assertEquals("phone_number", violation.getPropertyPath().toString());
        Assertions.assertEquals("Phone number must have exactly 9 digits and contain only numbers", violation.getMessage());
    }

    @Test
    public void testInvalidPhoneNumberLengthToBig() {
        // Given
        String invalidPhoneNumber = "1234564324333";
        Student student = new Student();
        student.setPhone_number(invalidPhoneNumber);

        // When/Then
        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        ConstraintViolation<Student> violation = violations.iterator().next();
        Assertions.assertEquals("phone_number", violation.getPropertyPath().toString());
        Assertions.assertEquals("Phone number must have exactly 9 digits and contain only numbers", violation.getMessage());
    }

    @Test
    public void testInvalidPhoneNumberLetters() {
        String invalidPhoneNumber = "fdsfasdnlkns dsa";
        Student student = new Student();
        student.setPhone_number(invalidPhoneNumber);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        ConstraintViolation<Student> violation = violations.iterator().next();
        Assertions.assertEquals("phone_number", violation.getPropertyPath().toString());
        Assertions.assertEquals("Phone number must have exactly 9 digits and contain only numbers", violation.getMessage());
    }
    @Test
    public void testUpdateStudent() {
        // Given
        Long studentId = 1L;
        Student existingStudent = new Student("Leo", "Messi", "messi123@gmail.com", "567654567");
        existingStudent.setId(studentId);

        Student updatedData = new Student("Updated", "updatedStudent", "updated@student.com", "123456789");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Student updatedStudent = studentService.updateStudent(studentId, updatedData);

        // Then
        Assertions.assertEquals(updatedData.getFirst_name(), updatedStudent.getFirst_name());
        Assertions.assertEquals(updatedData.getLast_name(), updatedStudent.getLast_name());
        Assertions.assertEquals(updatedData.getEmail(), updatedStudent.getEmail());
        Assertions.assertEquals(updatedData.getPhone_number(), updatedStudent.getPhone_number());

    }

}
