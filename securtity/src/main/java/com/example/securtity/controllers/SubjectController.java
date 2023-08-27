package com.example.securtity.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securtity.dto.SubjectDto;
import com.example.securtity.models.Professor;
import com.example.securtity.models.Subject;
import com.example.securtity.response.ApiResponse;
import com.example.securtity.service.SubjectService;

@RestController
@RequestMapping("/api/admin/subject")
public class SubjectController {
    
    @Autowired
    private SubjectService subjectService;

    @GetMapping("get")
    public ResponseEntity<List<Subject>> getAllSubjects(){
        return subjectService.getAllSubjects();
    }

    @GetMapping("get/{id}")
    public ResponseEntity<ApiResponse<Subject>> getSubjectById(@PathVariable Long id){
        return subjectService.getSubjectById(id);
    }

    @PostMapping("save")
    public ResponseEntity<String> addSubject(@RequestBody SubjectDto subjectDto){
        return subjectService.addSubject(subjectDto);
    }

    @PatchMapping("{subjectId}/add/professor")
    public ResponseEntity<String> addProfessor(@PathVariable Long subjectId, @RequestBody Professor professor){
        return subjectService.addProfessor(subjectId, professor);
    }
}
