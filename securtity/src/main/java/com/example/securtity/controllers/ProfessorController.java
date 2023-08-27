package com.example.securtity.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securtity.models.Professor;
import com.example.securtity.response.ApiResponse;
import com.example.securtity.service.ProfessorService;

@RestController
@RequestMapping("/api/admin/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;
    
    @GetMapping("get")
    public List<Professor> getAllProfessors(){
        return professorService.getAllProfessors();
    }

    @GetMapping("get/{id}")
    public ResponseEntity<ApiResponse<Professor>> getProfessor(@PathVariable Long id){
        return professorService.getProfessor(id);
    }

    @PostMapping("save")
    public ResponseEntity<String> addProfessor(@RequestBody Professor professor){
        return professorService.addProfessor(professor);
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<String> editProfessor(@PathVariable Long id, @RequestBody Professor professor){
        return professorService.editProfessor(id, professor);
    }
}
