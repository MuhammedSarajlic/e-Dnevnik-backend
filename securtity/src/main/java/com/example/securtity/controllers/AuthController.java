package com.example.securtity.controllers;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securtity.dto.AuthResponseDto;
import com.example.securtity.dto.LoginDto;
import com.example.securtity.dto.RegisterDto;
import com.example.securtity.models.Role;
import com.example.securtity.models.UserEntity;
import com.example.securtity.repository.RoleRepository;
import com.example.securtity.repository.UserRepository;
import com.example.securtity.security.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtService.generateToken(authentication);
            return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }
    
    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto ) {
        if(userRepository.existsByUsername(registerDto.getUsername())){
            return new ResponseEntity<>("Username already exist", HttpStatus.BAD_REQUEST);
        }

        if(registerDto.getPassword().length() < 8){
            return new ResponseEntity<>("Password is too short", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Optional<Role> role = roleRepository.findByName("ADMIN");
        if(role.isPresent()){
            user.setRoles(Collections.singletonList(role.get()));
        }

        userRepository.save(user);
        return new ResponseEntity<>("User registered", HttpStatus.OK);
    }
}
