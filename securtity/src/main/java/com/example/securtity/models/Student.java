package com.example.securtity.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Student {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String dob;
    private String phoneNumber;
    @JsonIgnoreProperties("students")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id")
    @Nullable
    private ClassEntity classEntity;
    @JsonIgnoreProperties("student")
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Grade> grades;
}
