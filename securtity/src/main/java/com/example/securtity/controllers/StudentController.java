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

import com.example.securtity.dto.StudentDto;
import com.example.securtity.models.Student;
import com.example.securtity.response.ApiResponse;
import com.example.securtity.service.StudentService;

@RestController
@RequestMapping("/api/admin/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("get")
    public List<Student> getAllStudents(){
        return studentService.getAllStudents();
    }

    @GetMapping("get/students-wtihout-class")
    public List<Student> getStudentsWithoutClass(){
        return studentService.getStudentsWithoutClass();
    }

    @GetMapping("get/{id}")
    public ResponseEntity<ApiResponse<Student>> getStudent(@PathVariable Long id){
        return studentService.getStudent(id);
    }

    @PostMapping("save")
    public ResponseEntity<String> addStudent(@RequestBody StudentDto studentDto){
        return studentService.addStudent(studentDto);
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<String> editStudent(@PathVariable Long id, @RequestBody StudentDto studentDto){
        return studentService.editStudent(id, studentDto);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id){
        return studentService.deleteStudent(id);
    }
}
