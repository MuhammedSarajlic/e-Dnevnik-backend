package com.example.securtity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.securtity.models.Major;


public interface MajorRepository extends JpaRepository<Major, Long> {
    Major findByDepartment(int department);
}
