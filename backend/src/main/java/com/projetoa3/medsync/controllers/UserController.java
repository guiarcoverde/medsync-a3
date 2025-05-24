package com.projetoa3.medsync.controllers;

import com.projetoa3.medsync.domain.user.User;
import com.projetoa3.medsync.dtos.UpdateUserDTO;
import com.projetoa3.medsync.exceptions.EmailAlreadyExistsException;
import com.projetoa3.medsync.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            userService.save(user);
            return ResponseEntity.ok("Usuário cadastrado com sucesso");
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateUserDTO user) {
        try {
            userService.update(id, user);
            return ResponseEntity.ok("Usuário atualizado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }
}
