package com.example.securtity.dto;

import com.example.securtity.models.Professor;

import lombok.Data;

@Data
public class SubjectDto {
    private String name;
    private Professor professor;
}
