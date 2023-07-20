package com.example.demo.UnitTests;


import com.example.demo.SchoolManagementSystemApplication;
import com.example.demo.entity.Teacher;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.service.Implementation.TeacherServiceImpl;
import com.example.demo.service.TeacherService;
import jakarta.transaction.Transactional;
import jakarta.validation.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SchoolManagementSystemApplication.class)
@Transactional
public class TeacherTest {

    @Test
    void contextLoads() {
    }

    private TeacherRepository teacherRepository;
    private TeacherService teacherService;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        teacherRepository = Mockito.mock(TeacherRepository.class);
        teacherService = new TeacherServiceImpl(teacherRepository);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testFindAllTeachers() {
        List<Teacher> expectedTeachers = new ArrayList<>();
        expectedTeachers.add(new Teacher("Leo", "Messi", "messi123@gmail.com", "567654567"));
        expectedTeachers.add(new Teacher("Erling", "Halland", "erling.halland@city.com", "765678999"));
        when(teacherRepository.findAll()).thenReturn(expectedTeachers);

        List<Teacher> actualTeachers = teacherService.findAllTeachers();

        Assertions.assertEquals(expectedTeachers.size(), actualTeachers.size());
        Assertions.assertEquals(expectedTeachers, actualTeachers);
    }

    @Test
    public void testCreateTeacher() {
        Teacher teacher = new Teacher("Leo", "Messi", "messi123@gmail.com", "567654567");
        when(teacherRepository.save(Mockito.any(Teacher.class))).thenReturn(teacher);

        Teacher savedTeacher = teacherService.createTeacher(teacher);

        Assertions.assertEquals(teacher, savedTeacher);
    }

    @Test
    public void testDeleteTeacher() {
        // Given
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(java.util.Optional.of(new Teacher("Leo", "Messi", "messi123@gmail.com", "567654567")));
        Mockito.doNothing().when(teacherRepository).deleteById(teacherId);

        // When
        String result = teacherService.deleteTeacher(teacherId);

        // Then
        Assertions.assertEquals("Teacher with id: " + teacherId + ", deleted", result);
    }

    @Test
    public void testCreateTeacherWithNullName() {
        // Given
        Teacher teacher = new Teacher(null, "Messi", "messi123@gmail.com", "567654567");

        assertThrows(IllegalArgumentException.class, () -> teacherService.createTeacher(teacher));
    }

    @Test
    public void testCreateTeacherWithNullLastName() {
        // Given
        Teacher teacher = new Teacher("Leo", null, "messi123@gmail.com", "567654567");

        assertThrows(NullPointerException.class, () -> teacherService.createTeacher(teacher));
    }

    @Test
    public void testCreateTeacherWithNullPhone() {
        // Given
        Teacher teacher = new Teacher("Leo", "Messi", "messi123@gmail.com", null);

        assertThrows(NullPointerException.class, () -> teacherService.createTeacher(teacher));
    }

    @Test
    public void testCreateTeacherWithNullEmail() {
        Teacher teacher = new Teacher("Leo", "Messi", null, "345323432");

        assertThrows(NullPointerException.class, () -> teacherService.createTeacher(teacher));

    }

    @Test
    public void testInvalidEmail() {
        //Given
        String invalidEmail = "invalid.email";
        Teacher teacher = new Teacher();
        teacher.setEmail(invalidEmail);

        //When/Then
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        ConstraintViolation<Teacher> violation = violations.iterator().next();
        Assertions.assertEquals("email", violation.getPropertyPath().toString());
        Assertions.assertEquals("Invalid email address format", violation.getMessage());
    }

    @Test
    public void testInvalidLastName() {
        String invalidLastName = "123";
        Teacher teacher = new Teacher();
        teacher.setLast_name(invalidLastName);

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        ConstraintViolation<Teacher> violation = violations.iterator().next();
        Assertions.assertEquals("last_name", violation.getPropertyPath().toString());
        Assertions.assertEquals("Last name should contain only letters", violation.getMessage());
    }

    @Test
    public void testInvalidFirstName() {
        String invalidFirstName = "John123";
        Teacher teacher = new Teacher();
        teacher.setFirst_name(invalidFirstName);

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        ConstraintViolation<Teacher> violation = violations.iterator().next();
        Assertions.assertEquals("first_name", violation.getPropertyPath().toString());
        Assertions.assertEquals("First name should contain only letters", violation.getMessage());
    }

    @Test
    public void testInvalidPhoneNumberLengthToShort() {
        String invalidPhoneNumber = "123";
        Teacher teacher = new Teacher();
        teacher.setPhone_number(invalidPhoneNumber);

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        ConstraintViolation<Teacher> violation = violations.iterator().next();
        Assertions.assertEquals("phone_number", violation.getPropertyPath().toString());
        Assertions.assertEquals("Phone number must have exactly 9 digits and contain only numbers", violation.getMessage());
    }

    @Test
    public void testInvalidPhoneNumberLengthToBig() {
        String invalidPhoneNumber = "1234564324333";
        Teacher teacher = new Teacher();
        teacher.setPhone_number(invalidPhoneNumber);

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        ConstraintViolation<Teacher> violation = violations.iterator().next();
        Assertions.assertEquals("phone_number", violation.getPropertyPath().toString());
        Assertions.assertEquals("Phone number must have exactly 9 digits and contain only numbers", violation.getMessage());
    }

    @Test
    public void testInvalidPhoneNumberLetters() {
        String invalidPhoneNumber = "fdsfasdnlkns dsa";
        Teacher teacher = new Teacher();
        teacher.setPhone_number(invalidPhoneNumber);

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        ConstraintViolation<Teacher> violation = violations.iterator().next();
        Assertions.assertEquals("phone_number", violation.getPropertyPath().toString());
        Assertions.assertEquals("Phone number must have exactly 9 digits and contain only numbers", violation.getMessage());
    }

    @Test
    public void testUpdateTeacher() {
        Long teacherId = 1L;
        Teacher existingTeacher = new Teacher("Antonio", "Banderas", "agent007@wp.pl", "565456789");
        existingTeacher.setId(teacherId);

        Teacher updatedData = new Teacher("UpdatedName", "updatedLastName", "updated@example.com", "95969594939");

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(existingTeacher));
        when(teacherRepository.save(any(Teacher.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Teacher updatedTeacher = teacherService.updateTeacher(teacherId, updatedData);

        Assertions.assertEquals(updatedData.getFirst_name(), updatedTeacher.getFirst_name());
        Assertions.assertEquals(updatedData.getLast_name(), updatedTeacher.getLast_name());
        Assertions.assertEquals(updatedData.getEmail(), updatedTeacher.getEmail());
        Assertions.assertEquals(updatedData.getPhone_number(), updatedTeacher.getPhone_number());
    }
}
