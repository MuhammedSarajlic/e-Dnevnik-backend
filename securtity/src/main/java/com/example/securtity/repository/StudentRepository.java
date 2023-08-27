package com.example.securtity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.securtity.models.Student;

public interface StudentRepository extends JpaRepository <Student, Long> {
    Boolean existsByEmail(String email);
    List<Student> findByClassEntityIsNull();
}
