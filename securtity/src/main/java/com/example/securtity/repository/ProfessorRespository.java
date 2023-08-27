package com.example.securtity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.securtity.models.Professor;


public interface ProfessorRespository extends JpaRepository<Professor, Long> {
    Professor findByEmail(String email);
    boolean existsByEmail(String email);
}
