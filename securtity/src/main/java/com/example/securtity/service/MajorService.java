package com.example.securtity.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.securtity.dto.MajorDto;
import com.example.securtity.models.Major;
import com.example.securtity.models.Subject;
import com.example.securtity.repository.MajorRepository;
import com.example.securtity.repository.SubjectRepository;

@Service
public class MajorService {

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    public List<Major> getMajors(){
        return majorRepository.findAll();
    }

    public Optional<Major> getMajor(Long id){
        return majorRepository.findById(id);
    }

    public ResponseEntity<String> saveMajor(MajorDto majorDto){
        Major major = new Major();
        major.setName(majorDto.getName());
        major.setDepartment(majorDto.getDepartment());

        majorRepository.save(major);
        return new ResponseEntity<>("Major added.", HttpStatus.OK);
    }

    public ResponseEntity<String> addSubjects(Long majorId, List<Subject> subjects) {
        Optional<Major> tempMajorOpotional = majorRepository.findById(majorId);
        if(tempMajorOpotional.isEmpty()){
            return new ResponseEntity<>("Major not found.", HttpStatus.NOT_FOUND);
        }
        List<Subject> subjectArr = new ArrayList<>();
        for(Subject subject : subjects){
            Optional<Subject> tempSubject = subjectRepository.findById(subject.getId());
            subjectArr.add(tempSubject.get());
        }
        tempMajorOpotional.get().getSubjects().addAll(subjectArr);
        majorRepository.save(tempMajorOpotional.get());
        return new ResponseEntity<>("Subject added.", HttpStatus.OK);
    }

    public ResponseEntity<String> removeSubject(Long majorId, Long subjectId) {
        Optional<Major> tempMajorOptional = majorRepository.findById(majorId);
        Optional<Subject> tempSubjectOptional = subjectRepository.findById(subjectId);
        if(tempMajorOptional.isEmpty() || tempSubjectOptional.isEmpty()){
            return new ResponseEntity<>("Major not found", HttpStatus.NOT_FOUND);
        }
        if(!tempMajorOptional.get().getSubjects().contains(tempSubjectOptional.get())){
            return new ResponseEntity<>("Subject not found in this major", HttpStatus.NOT_FOUND);
        }
        tempMajorOptional.get().getSubjects().remove(tempSubjectOptional.get());
        majorRepository.save(tempMajorOptional.get());
        return new ResponseEntity<>("Subject removed.", HttpStatus.OK);
    }
}
