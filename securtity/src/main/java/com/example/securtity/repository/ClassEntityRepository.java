package com.example.securtity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.securtity.models.ClassEntity;

import java.util.List;

public interface ClassEntityRepository extends JpaRepository<ClassEntity, Long> {
    List<ClassEntity> findAllByOrderByYearAsc();
}
