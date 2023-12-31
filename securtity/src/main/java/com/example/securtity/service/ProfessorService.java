package com.example.securtity.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.securtity.models.ClassEntity;
import com.example.securtity.repository.ClassEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.securtity.models.Professor;
import com.example.securtity.models.Role;
import com.example.securtity.models.UserEntity;
import com.example.securtity.repository.ProfessorRespository;
import com.example.securtity.repository.RoleRepository;
import com.example.securtity.repository.UserRepository;
import com.example.securtity.response.ApiResponse;

@Service
public class ProfessorService {
    
    @Autowired
    private ProfessorRespository professorRespository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClassEntityRepository classEntityRepository;

    public List<Professor> getAllProfessors(){
        return professorRespository.findAll();
    }

    public ResponseEntity<ApiResponse<Professor>> getProfessor(Long id) {
        Optional<Professor> professor = professorRespository.findById(id);
        ApiResponse<Professor> response = new ApiResponse<>();
        if(!professor.isPresent()){
            response.setErrorMessage("Professor not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.setData(professor.get());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> addProfessor(Professor professor) {
        if(professorRespository.existsByEmail(professor.getEmail())){
            return new ResponseEntity<>("Professor with this email already exist.", HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByUsername(professor.getEmail())){
            return new ResponseEntity<>("Username already exist", HttpStatus.BAD_REQUEST);
        }

        Professor newProfessor = new Professor();
        newProfessor.setFirstName(professor.getFirstName());
        newProfessor.setLastName(professor.getLastName());
        newProfessor.setEmail(professor.getEmail());
        if(professor.getAssignedClass().getId() != null){
            Optional<ClassEntity> tempClassOptional = classEntityRepository.findById(professor.getAssignedClass().getId());
            tempClassOptional.get().setProfessor(newProfessor);
            newProfessor.setAssignedClass(tempClassOptional.get());
        }

        professorRespository.save(newProfessor);


        String pass = professor.getFirstName().toLowerCase() + professor.getLastName().toLowerCase();
        UserEntity user = new UserEntity();
        user.setUsername(professor.getEmail());
        user.setPassword(passwordEncoder.encode(pass));

        Optional<Role> role = roleRepository.findByName("PROFESSOR");
        if(role.isPresent()){
            user.setRoles(Collections.singletonList(role.get()));
        }

        userRepository.save(user);
        return new ResponseEntity<>("Professor added.", HttpStatus.OK);
    }

    public ResponseEntity<String> editProfessor(Long id, Professor professor) {
        Optional<Professor> existingProfessor = professorRespository.findById(id);
        if(existingProfessor.isEmpty()){
            return new ResponseEntity<>("Professor not found", HttpStatus.NOT_FOUND);
        }
        Professor tempProfessor = existingProfessor.get();
        tempProfessor.setFirstName(professor.getFirstName());
        tempProfessor.setLastName(professor.getLastName());
        tempProfessor.setEmail(professor.getEmail());
        
        professorRespository.save(tempProfessor);
        return new ResponseEntity<>("Professor edited.", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteProfessor(List<Long> professorIdList) {
        for(Long id : professorIdList){
            Optional<Professor> existingProfessor = professorRespository.findById(id);
            if(existingProfessor.isEmpty()){
                return new ResponseEntity<>("Professor not found", HttpStatus.NOT_FOUND);
            }
            if(existingProfessor.get().getAssignedClass() != null){
                Optional<ClassEntity> tempClassOptional = classEntityRepository.findById(existingProfessor.get().getAssignedClass().getId());
                if(tempClassOptional.isPresent()){
                    tempClassOptional.get().setProfessor(null);
                }
            }
            Optional<UserEntity> tempUserOptional = userRepository.findByUsername(existingProfessor.get().getEmail());
            if(tempUserOptional.isEmpty()){
                return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
            }
            userRepository.deleteById(tempUserOptional.get().getId());
            professorRespository.deleteById(id);
        }
        return new ResponseEntity<>("Professor deleted", HttpStatus.OK);
    }
}
