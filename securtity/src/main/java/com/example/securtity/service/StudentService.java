package com.example.securtity.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.securtity.dto.StudentDto;
import com.example.securtity.models.Role;
import com.example.securtity.models.Student;
import com.example.securtity.models.UserEntity;
import com.example.securtity.repository.RoleRepository;
import com.example.securtity.repository.StudentRepository;
import com.example.securtity.repository.UserRepository;
import com.example.securtity.response.ApiResponse;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public ResponseEntity<ApiResponse<Student>> getStudent(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        ApiResponse<Student> response = new ApiResponse<>();
        if(student.isEmpty()){
            response.setErrorMessage("User not found");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        response.setData(student.get());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<String> addStudent(@RequestBody StudentDto studentDto){
        if(studentRepository.existsByEmail(studentDto.getEmail())){
            return new ResponseEntity<>("Student with that email already exist", HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByUsername(studentDto.getEmail())){
            return new ResponseEntity<>("Username already exist", HttpStatus.BAD_REQUEST);
        }
        Student student = new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setDob(studentDto.getDob());
        student.setAddress(studentDto.getAddress());
        student.setEmail(studentDto.getEmail());
        student.setPhoneNumber(studentDto.getPhoneNumber());

        studentRepository.save(student);
        

        String pass = studentDto.getFirstName().toLowerCase() + studentDto.getLastName().toLowerCase();
        UserEntity user = new UserEntity();
        user.setUsername(studentDto.getEmail());
        user.setPassword(passwordEncoder.encode(pass));

        Optional<Role> role = roleRepository.findByName("STUDENT");
        if(role.isPresent()){
            user.setRoles(Collections.singletonList(role.get()));
        }

        userRepository.save(user);

        return new ResponseEntity<>("Student saved.", HttpStatus.OK);
    }

    public ResponseEntity<String> editStudent(Long id, StudentDto studentDto) {
        Optional<Student> student = studentRepository.findById(id);
        if(student.isEmpty()){
            return new ResponseEntity<>("Student not found.", HttpStatus.NOT_FOUND);
        }
        Student tempStudent = student.get();
        tempStudent.setFirstName(studentDto.getFirstName());
        tempStudent.setLastName(studentDto.getLastName());
        tempStudent.setDob(studentDto.getDob());
        tempStudent.setAddress(studentDto.getAddress());
        tempStudent.setEmail(studentDto.getEmail());
        tempStudent.setPhoneNumber(studentDto.getPhoneNumber());

        studentRepository.save(tempStudent);
        return new ResponseEntity<>("Student changed.", HttpStatus.OK) ;
    }

    public List<Student> getStudentsWithoutClass() {
        return studentRepository.findByClassEntityIsNull();
    }

    public ResponseEntity<String> deleteStudent(Long id) {
        Optional<Student> tempStudentOptional = studentRepository.findById(id);
        if(tempStudentOptional.isEmpty()){
            return new ResponseEntity<>("Studdent not found.", HttpStatus.NOT_FOUND);
        }
        Optional<UserEntity> tempUserOptional = userRepository.findByUsername(tempStudentOptional.get().getEmail());
        if(tempUserOptional.isEmpty()){
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
        userRepository.delete(tempUserOptional.get());
        studentRepository.deleteById(id);
        return new ResponseEntity<>("Student deleted.", HttpStatus.OK);
    }

}
