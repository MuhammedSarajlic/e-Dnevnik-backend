package com.example.securtity.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.securtity.dto.ClassEntityDto;
import com.example.securtity.models.ClassEntity;
import com.example.securtity.models.Major;
import com.example.securtity.models.Professor;
import com.example.securtity.models.Student;
import com.example.securtity.repository.ClassEntityRepository;
import com.example.securtity.repository.MajorRepository;
import com.example.securtity.repository.ProfessorRespository;
import com.example.securtity.repository.StudentRepository;
import com.example.securtity.response.ApiResponse;

import jakarta.transaction.Transactional;

@Service
public class ClassEntityService {

    @Autowired
    private ClassEntityRepository classRepository;

    @Autowired
    private MajorRepository majorRepository;
    
    @Autowired
    private ProfessorRespository professorRespository;

    @Autowired
    private StudentRepository studentRepository;
    
    public List<ClassEntity> getClasses() {
        return classRepository.findAllByOrderByYearAsc();
    }

    public List<ClassEntity> getClassesWithoutProfessor() {
        return classRepository.findByProfessorIsNull();
    }

    public ResponseEntity<ApiResponse<ClassEntity>> getClassById(Long id) {
        Optional<ClassEntity> myClass = classRepository.findById(id);
        ApiResponse<ClassEntity> response = new ApiResponse<>();
        if(myClass.isEmpty()){
            response.setErrorMessage("Class not found");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        response.setData(myClass.get());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<String> addClassEntity(ClassEntityDto classEntityDto){
        
        ClassEntity classEntity = new ClassEntity();
        Major major = majorRepository.findByDepartment(classEntityDto.getMajor().getDepartment());
        classEntity.setMajor(major);
        classEntity.setYear(classEntityDto.getYear());
        if(classEntityDto.getProfessor() != null){
            Professor professor = professorRespository.findByEmail(classEntityDto.getProfessor().getEmail());
            classEntity.setProfessor(professor);
            professor.setAssignedClass(classEntity);
            professorRespository.save(professor);
        }

        classRepository.save(classEntity);
        return new ResponseEntity<>("Class added", HttpStatus.OK);
    }

    public ResponseEntity<String> editClass(Long id, ClassEntityDto classEntityDto) {
        Optional<ClassEntity> myClass = classRepository.findById(id);
        if(!myClass.isPresent()){
            return new ResponseEntity<>("Class not found.", HttpStatus.BAD_REQUEST);
        }
        
        ClassEntity newClass = myClass.get();
        newClass.setMajor(classEntityDto.getMajor());
        newClass.setYear(classEntityDto.getYear());
        newClass.setProfessor(classEntityDto.getProfessor());
        newClass.setStudents(classEntityDto.getStudents());

        for(Student student : classEntityDto.getStudents()){
            Optional<Student> tempStudent = studentRepository.findById(student.getId());
            if(!tempStudent.isPresent()){
                return new ResponseEntity<>("Student not found.", HttpStatus.BAD_REQUEST);
            }
            tempStudent.get().setClassEntity(newClass);
            studentRepository.save(tempStudent.get());
        }

        classRepository.save(newClass);
        return new ResponseEntity<>("Class saved. ", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> deleteClass(Long id) {
        Optional<ClassEntity> tempClass = classRepository.findById(id);
        if(tempClass.isEmpty()){
            return new ResponseEntity<>("Class not found.", HttpStatus.BAD_REQUEST);
        }
        Professor professor = tempClass.get().getProfessor();
        if(professor != null){
            professor.setAssignedClass(null);
            professorRespository.save(professor);
        }
        classRepository.deleteById(id);
        return new ResponseEntity<>("Class deleted.", HttpStatus.OK);
    }

    public ResponseEntity<String> removeStudent(Long classId, Long studentId) {
        Optional<ClassEntity> tempClass = classRepository.findById(classId);
        Optional<Student> tempStudent = studentRepository.findById(studentId);
        if(tempClass.isEmpty() || tempStudent.isEmpty()){
            return new ResponseEntity<>("Class or student not found.", HttpStatus.NOT_FOUND);
        }

        ClassEntity currentClass = tempClass.get();
        List<Student> students = currentClass.getStudents();
        students.removeIf(student -> student.getId().equals(studentId));

        Student student = tempStudent.get();
        student.setClassEntity(null);

        studentRepository.save(student);
        classRepository.save(currentClass);
        return new ResponseEntity<>("Student removed.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> addStudent(Long classId, List<Student> students) {
        Optional<ClassEntity> tempClassOptional = classRepository.findById(classId);
        if(tempClassOptional.isEmpty()){
            return new ResponseEntity<>("Class or student not found.", HttpStatus.NOT_FOUND);
        }
        tempClassOptional.get().getStudents().addAll(students);
        for(Student student : students){
            Optional<Student> tempStudentOptional = studentRepository.findById(student.getId());
            if(tempStudentOptional.isEmpty()){
                return new ResponseEntity<>("Student not found.", HttpStatus.NOT_FOUND);
            }
            tempStudentOptional.get().setClassEntity(tempClassOptional.get());
            studentRepository.save(tempStudentOptional.get());
        }
        classRepository.save(tempClassOptional.get());
        return new ResponseEntity<>("Student added.", HttpStatus.OK);
    }

    public ResponseEntity<String> removeProfessor(Long classId) {
        Optional<ClassEntity> tempOptionalClass = classRepository.findById(classId);
        if(tempOptionalClass.isEmpty()){
            return new ResponseEntity<>("Class or professor not found.", HttpStatus.NOT_FOUND);
        }
        tempOptionalClass.get().getProfessor().setAssignedClass(null);
        tempOptionalClass.get().setProfessor(null);
        classRepository.save(tempOptionalClass.get());
        return new ResponseEntity<>("Professor removed.", HttpStatus.OK);
    }

    public ResponseEntity<String> addProfessor(Long classId, Professor professor) {
        Optional<ClassEntity> tempOptionalClass = classRepository.findById(classId);
        Optional<Professor> tempOptionalProfessor = professorRespository.findById(professor.getId());
        if(tempOptionalClass.isEmpty() || tempOptionalProfessor.isEmpty()){
            return new ResponseEntity<>("Class or professor not found.", HttpStatus.NOT_FOUND);
        }
        if(tempOptionalClass.get().getProfessor() != null){
            return new ResponseEntity<>("Professor already added.", HttpStatus.BAD_REQUEST);
        }
        tempOptionalClass.get().setProfessor(tempOptionalProfessor.get());
        classRepository.save(tempOptionalClass.get());
        return new ResponseEntity<>("Professor added.", HttpStatus.OK);
    }
}
