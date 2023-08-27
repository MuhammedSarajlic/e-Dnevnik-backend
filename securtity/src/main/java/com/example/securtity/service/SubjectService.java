package com.example.securtity.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.securtity.dto.SubjectDto;
import com.example.securtity.models.Professor;
import com.example.securtity.models.Subject;
import com.example.securtity.repository.ProfessorRespository;
import com.example.securtity.repository.SubjectRepository;
import com.example.securtity.response.ApiResponse;

import jakarta.transaction.Transactional;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ProfessorRespository professorRespository;

    public ResponseEntity<List<Subject>> getAllSubjects() {
        return new ResponseEntity<>(subjectRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<Subject>> getSubjectById(Long id) {
        Optional<Subject> tempSubjectOptional = subjectRepository.findById(id);
        ApiResponse<Subject> response = new ApiResponse<>();
        if(tempSubjectOptional.isEmpty()){
            response.setErrorMessage("Subject not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.setData(tempSubjectOptional.get());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
	public ResponseEntity<String> addSubject(SubjectDto subjectDto) {
        Subject subject = new Subject();
        subject.setName(subjectDto.getName());
        if(subjectDto.getProfessor() != null){
            Optional<Professor> tempProfessorOptional = professorRespository.findById(subjectDto.getProfessor().getId());
            if(tempProfessorOptional.isPresent()){
                Professor tempProfessor = tempProfessorOptional.get();
                subject.setProfessor(tempProfessor);
                tempProfessor.getSubjects().add(subject);
                professorRespository.save(tempProfessor);
            }
        }
        subjectRepository.save(subject);
		return new ResponseEntity<>("Subject added.", HttpStatus.OK);
	}

    public ResponseEntity<String> addProfessor(Long subjectId, Professor professor) {
        Optional<Subject> tempSubjectOptional = subjectRepository.findById(subjectId);
        Optional<Professor> tempProfessorOptional = professorRespository.findById(professor.getId());
        if(tempSubjectOptional.isEmpty() || tempProfessorOptional.isEmpty()){
            return new ResponseEntity<>("Subject or professor not found.", HttpStatus.NOT_FOUND);
        }
        tempSubjectOptional.get().setProfessor(tempProfessorOptional.get());
        subjectRepository.save(tempSubjectOptional.get());
        tempProfessorOptional.get().getSubjects().add(tempSubjectOptional.get());
        professorRespository.save(tempProfessorOptional.get());
        return new ResponseEntity<>("Professor added.", HttpStatus.OK);
    }

    
}
