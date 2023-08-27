package com.example.securtity.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnoreProperties("professor")
    @OneToOne(mappedBy = "professor", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Nullable
    private ClassEntity assignedClass;
    @JsonIgnoreProperties("professor")
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "subject_professor",
                joinColumns = @JoinColumn(name = "professor_id"),
                inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects;
}
