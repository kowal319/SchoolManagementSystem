package com.example.demo.service;


import com.example.demo.entity.Subject;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SubjectService {

    List<Subject> findAllSubjects();
    Subject findById(Long id);
    Subject createSubject(Subject subject);
    String deleteSubject(Long id);
    Subject updateSubject(Long id, Subject updatedSubject);
}
