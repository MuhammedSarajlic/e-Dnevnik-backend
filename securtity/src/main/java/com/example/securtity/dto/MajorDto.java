package com.example.securtity.dto;

import java.util.List;

import com.example.securtity.models.Subject;

import lombok.Data;

@Data
public class MajorDto {
    private String name;
    private int department;
    private List<Subject> subjects;
}
