package com.example.securtity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.securtity.models.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    
}
