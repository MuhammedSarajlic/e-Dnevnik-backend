package com.example.securtity.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securtity.dto.ClassEntityDto;
import com.example.securtity.models.ClassEntity;
import com.example.securtity.models.Professor;
import com.example.securtity.models.Student;
import com.example.securtity.response.ApiResponse;
import com.example.securtity.service.ClassEntityService;

@RestController
@RequestMapping("/api/admin/class")
public class ClassEntityController {
    
    @Autowired
    private ClassEntityService classService;

    @GetMapping("get")
    public List<ClassEntity> getClasses(){
        return classService.getClasses();
    }

    @GetMapping("get/{id}")
    public ResponseEntity<ApiResponse<ClassEntity>> getClassById(@PathVariable Long id){
        return classService.getClassById(id);
    }

    @PostMapping("save")
    public ResponseEntity<String> addClassEntity(@RequestBody ClassEntityDto classEntityDto){
        return classService.addClassEntity(classEntityDto);
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<String> editClass(@PathVariable Long id, @RequestBody ClassEntityDto classEntityDto){
        return classService.editClass(id, classEntityDto);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteClass(@PathVariable Long id){
        return classService.deleteClass(id);
    }

    @DeleteMapping("{classId}/delete/student/{studentId}")
    public ResponseEntity<String> removeStudent(@PathVariable Long classId, @PathVariable Long studentId){
        return classService.removeStudent(classId, studentId);
    }

    @PostMapping("{classId}/add/student")
    public ResponseEntity<String> addStudent(@PathVariable Long classId, @RequestBody List<Student> students){
        return classService.addStudent(classId, students);
    }

    @DeleteMapping("{classId}/delete/professor")
    public ResponseEntity<String> removeProfessor(@PathVariable Long classId){
        return classService.removeProfessor(classId);
    }

    @PostMapping("{classId}/add/professor")
    public ResponseEntity<String> addProfessor(@PathVariable Long classId, @RequestBody Professor professor){
        return classService.addProfessor(classId, professor);
    }

}
