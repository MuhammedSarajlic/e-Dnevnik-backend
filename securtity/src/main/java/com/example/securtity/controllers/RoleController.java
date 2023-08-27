package com.example.securtity.controllers;

import com.example.securtity.models.Role;
import com.example.securtity.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping
    public String addRole(@RequestBody Role role){
        roleRepository.save(role);
        return "Saved";
    }
}
