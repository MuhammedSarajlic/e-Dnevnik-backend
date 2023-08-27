package com.example.securtity.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securtity.models.Grade;
import com.example.securtity.response.ApiResponse;
import com.example.securtity.service.GradeService;

@RestController
@RequestMapping("/api/professor/grade")
public class GradeController {
    
    @Autowired
    private GradeService gradeService;

    @GetMapping("get/{studentId}")
    public ResponseEntity<ApiResponse<List<Grade>>> getAllGradesForStudent(@PathVariable Long studentId){
        return gradeService.getAllGradesForStudent(studentId);
    }

    @PostMapping("save")
    public ResponseEntity<String> addGrade(@RequestBody Grade grade){
        return gradeService.addGrade(grade);
    }

    @PatchMapping("{gradeId}/edit")
    public ResponseEntity<String> editGrade(@PathVariable Long gradeId, @RequestBody Grade grade){
        return gradeService.editGrade(gradeId, grade);
    }

    @DeleteMapping("{gradeId}/delete")
    public ResponseEntity<String> deleteGrade(@PathVariable Long gradeId){
        return gradeService.deleteGrade(gradeId);
    }
}
