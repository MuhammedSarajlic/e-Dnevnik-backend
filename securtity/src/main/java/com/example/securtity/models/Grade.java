package com.example.securtity.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnoreProperties({"email", "address", "dob", "phoneNumber", "classEntity"})
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @JsonIgnoreProperties("professor")
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    private int gradeValue;
}
