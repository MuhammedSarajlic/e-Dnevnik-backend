package com.example.securtity.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.securtity.models.Grade;
import com.example.securtity.models.Student;
import com.example.securtity.models.Subject;
import com.example.securtity.repository.GradeRepository;
import com.example.securtity.repository.StudentRepository;
import com.example.securtity.repository.SubjectRepository;
import com.example.securtity.response.ApiResponse;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    public ResponseEntity<ApiResponse<List<Grade>>> getAllGradesForStudent(Long studentId) {
        ApiResponse<List<Grade>> response = new ApiResponse<>();
        Optional<Student> tempStudentOptional = studentRepository.findById(studentId);
        if(tempStudentOptional.isEmpty()){
            response.setErrorMessage("Student not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        List<Grade> tempGrades = gradeRepository.findByStudent(tempStudentOptional.get());
        response.setData(tempGrades);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<String> addGrade(Grade grade) {
        if(grade.getGradeValue() < 1 || grade.getGradeValue() > 5){
            return new ResponseEntity<>("Invalid grade.", HttpStatus.BAD_REQUEST);
        }
        Optional<Student> tempStudentOptional = studentRepository.findById(grade.getStudent().getId());
        if(tempStudentOptional.isEmpty()){
            return new ResponseEntity<>("Student not found.", HttpStatus.NOT_FOUND);
        }
        Optional<Subject> tempSubjectOptional = subjectRepository.findById(grade.getSubject().getId());
        if(tempSubjectOptional.isEmpty()){
            return new ResponseEntity<>("Subject not found.", HttpStatus.NOT_FOUND);
        }
        Grade tempGrade = new Grade();
        tempGrade.setStudent(grade.getStudent());
        tempGrade.setSubject(grade.getSubject());
        tempGrade.setGradeValue(grade.getGradeValue());
        gradeRepository.save(tempGrade);
        return new ResponseEntity<>("Grade addded.", HttpStatus.OK);
    }

    public ResponseEntity<String> editGrade(Long gradeId, Grade grade) {
        Optional<Grade> tempGradeOptional = gradeRepository.findById(gradeId);
        if(tempGradeOptional.isEmpty()){
            return new ResponseEntity<>("Grade not found.", HttpStatus.NOT_FOUND);
        }
        if(grade.getGradeValue() < 1 || grade.getGradeValue() > 5){
            return new ResponseEntity<>("Invalid grade.", HttpStatus.BAD_REQUEST);
        }
        tempGradeOptional.get().setGradeValue(grade.getGradeValue());
        gradeRepository.save(tempGradeOptional.get());
        return new ResponseEntity<>("Grade edited.", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteGrade(Long gradeId) {
        Optional<Grade> tempGradeOptional = gradeRepository.findById(gradeId);
        if(tempGradeOptional.isEmpty()){
            return new ResponseEntity<>("Grade not found.", HttpStatus.NOT_FOUND);
        }
        gradeRepository.deleteById(gradeId);
        return new ResponseEntity<>("Grade deleted.", HttpStatus.OK);
    } 
}
