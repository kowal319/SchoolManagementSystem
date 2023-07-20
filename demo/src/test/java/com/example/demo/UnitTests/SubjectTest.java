package com.example.demo.UnitTests;
import com.example.demo.SchoolManagementSystemApplication;
import com.example.demo.entity.Subject;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.service.Implementation.SubjectServiceImpl;
import com.example.demo.service.SubjectService;
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
public class SubjectTest {

    @Test
    void contextLoads() {
    }

    private SubjectRepository subjectRepository;
    private SubjectService subjectService;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        subjectRepository = Mockito.mock(SubjectRepository.class);
        subjectService = new SubjectServiceImpl(subjectRepository);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testFindAllSubjects() {
        // Given
        List<Subject> expectedSubjects = new ArrayList<>();
        expectedSubjects.add(new Subject("Mathematics", "Plus and Minus function"));
        expectedSubjects.add(new Subject("Physical Education", "Cooper run"));
        when(subjectRepository.findAll()).thenReturn(expectedSubjects);

        // When
        List<Subject> actualSubjects = subjectService.findAllSubjects();

        // Then
        Assertions.assertEquals(expectedSubjects.size(), actualSubjects.size());
        Assertions.assertEquals(expectedSubjects, actualSubjects);
    }

    @Test
    public void testCreateSubject() {
        // Given
        Subject subject = new Subject("Physical Education", "Cooper run");
        when(subjectRepository.save(Mockito.any(Subject.class))).thenReturn(subject);

        // When
        Subject savedSubject = subjectService.createSubject(subject);

        // Then
        Assertions.assertEquals(subject, savedSubject);
    }

    @Test
    public void testDeleteSubject() {
        // Given
        Long subjectId = 1L;
        when(subjectRepository.findById(subjectId)).thenReturn(java.util.Optional.of(new Subject("Physical Education", "Cooper run")));
        Mockito.doNothing().when(subjectRepository).deleteById(subjectId);

        // When
        String result = subjectService.deleteSubject(subjectId);

        // Then
        Assertions.assertEquals("Subject with id: " + subjectId + ", deleted", result);
    }

    @Test
    public void testCreateSubjectWithNullName() {
        // Given
        Subject subject = new Subject(null, "Cooper run");

        assertThrows(IllegalArgumentException.class, () -> subjectService.createSubject(subject));
    }
    @Test
    public void testCreateSubjectWithNullLastName() {
        // Given
        Subject subject = new Subject("Physical Education", null);

        assertThrows(NullPointerException.class, () -> subjectService.createSubject(subject));
    }
    @Test
    public void testInvalidNameToLong() {
        // Given
        String invalidName = "MathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematics"; // Invalid phone number with less than 9 digits
        Subject subject = new Subject();
        subject.setName(invalidName);

        // When/Then
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        ConstraintViolation<Subject> violation = violations.iterator().next();
        Assertions.assertEquals("name", violation.getPropertyPath().toString());
        Assertions.assertEquals("Name cannot be longer than 50 characters", violation.getMessage());
    }
    @Test
    public void testInvalidDescriptionToLong() {
        // Given
        String invalidDescrition = "MathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematics" +
                "MathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematics" +
                "MathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematics" +
                "MathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematics" +
                "MathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematics" +
                "MathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematics" +
                "MathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematics" +
                "MathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematics" +
                "MathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematicsMathematics" +
                "";
        Subject subject = new Subject();
        subject.setName(invalidDescrition);

        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        ConstraintViolation<Subject> violation = violations.iterator().next();
        Assertions.assertEquals("name", violation.getPropertyPath().toString());
        Assertions.assertEquals("Name cannot be longer than 50 characters", violation.getMessage());
    }

    @Test
    public void testUpdateSubject() {
        Long subjectId = 1L;
        Subject existingSubject = new Subject("Wood Work", "Creating simple table from wood");
        existingSubject.setId(subjectId);

        Subject updatedData = new Subject("Updated", "updatedStudent");

        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(existingSubject));
        when(subjectRepository.save(any(Subject.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Subject updatedSubject = subjectService.updateSubject(subjectId, updatedData);

        Assertions.assertEquals(updatedData.getName(), updatedSubject.getName());
        Assertions.assertEquals(updatedData.getDescription(), updatedSubject.getDescription());


    }
}