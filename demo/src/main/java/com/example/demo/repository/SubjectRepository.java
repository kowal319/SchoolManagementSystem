package com.example.demo.repository;

import com.example.demo.entity.Student;
import com.example.demo.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository  extends JpaRepository<Subject, Long> {

}