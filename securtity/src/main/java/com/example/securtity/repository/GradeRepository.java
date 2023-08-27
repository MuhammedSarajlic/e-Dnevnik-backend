package com.example.securtity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.securtity.models.Grade;
import com.example.securtity.models.Student;

import java.util.List;


public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudent(Student student);
}
