package com.devops.projeto_ac2.controller;

import com.devops.projeto_ac2.dto.UserDTO;
import com.devops.projeto_ac2.entity.User;
import com.devops.projeto_ac2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO savedUser = userService.save(userDTO);
        return ResponseEntity.ok().body(savedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> returnUser(@PathVariable Long id)
    {
        User user = userService.returnUser(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsuarios() {
        List<UserDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

}