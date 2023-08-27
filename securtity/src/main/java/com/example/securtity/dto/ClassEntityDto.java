package com.example.securtity.dto;

import java.util.List;

import com.example.securtity.models.Major;
import com.example.securtity.models.Professor;
import com.example.securtity.models.Student;

import lombok.Data;

@Data
public class ClassEntityDto {
    private Major major;
    private int year;
    private Professor professor;
    private List<Student> students;
}
