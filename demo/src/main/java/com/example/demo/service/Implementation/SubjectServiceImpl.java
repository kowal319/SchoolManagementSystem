package com.example.demo.service.Implementation;

import com.example.demo.entity.Subject;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.service.SubjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<Subject> findAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject findById(Long id) {
        Optional<Subject> subjectOptional = subjectRepository.findById(id);
        return subjectOptional.orElse(null);
    }

    @Override
    public Subject createSubject(Subject subject) {
        if(subject.getName() == null){
            throw new IllegalArgumentException("Field cannot be null");
        }
        if ( subject.getDescription() == null ) {
            throw new NullPointerException("Field cannot be null");
        }      if (subject.getName().length() > 50 || subject.getDescription().length() > 200){
            throw new IllegalArgumentException("field cannot be longer than this long chars");
        }

        return subjectRepository.save(subject);
    }

    @Override
    public String deleteSubject(Long id) {
        subjectRepository.deleteById(id);
        return "Subject with id: " + id + ", deleted";
    }

    @Override
    public Subject updateSubject(Long id, Subject updatedSubject) {
        Optional<Subject> subjectOptional = subjectRepository.findById(id);
        if (subjectOptional.isPresent()) {
            Subject existingSubject = subjectOptional.get();
            existingSubject.setName(updatedSubject.getName());
            existingSubject.setDescription(updatedSubject.getDescription());
            return subjectRepository.save(existingSubject);
        }else {
            return null;
    }
}
}
