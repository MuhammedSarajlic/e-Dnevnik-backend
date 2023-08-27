package com.example.securtity.controllers;

import java.util.List;
import java.util.Optional;

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

import com.example.securtity.dto.MajorDto;
import com.example.securtity.models.Major;
import com.example.securtity.models.Subject;
import com.example.securtity.service.MajorService;

@RestController
@RequestMapping("/api/admin/major")
public class MajorController {
    
    @Autowired
    private MajorService majorService;

    @GetMapping("get")
    public List<Major> getMajors(){
        return majorService.getMajors();
    }

    @GetMapping("get/{id}")
    public Optional<Major> getMajor(@PathVariable Long id){
        return majorService.getMajor(id);
    }

    @PostMapping("save")
    public ResponseEntity<String> saveMajor(@RequestBody MajorDto majorDto){
        return majorService.saveMajor(majorDto);
    }

    @PatchMapping("{majorId}/add/subject")
    public ResponseEntity<String> addSubjects(@PathVariable Long majorId, @RequestBody List<Subject> subjects){
        return majorService.addSubjects(majorId, subjects);
    }

    @DeleteMapping("{majorId}/remove/subject/{subjectId}")
    public ResponseEntity<String> removeSubject(@PathVariable Long majorId, @PathVariable Long subjectId){
        return majorService.removeSubject(majorId, subjectId);
    }
}
