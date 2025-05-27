package com.projetoa3.medsync.controllers;

import com.projetoa3.medsync.domain.user.User;
import com.projetoa3.medsync.dtos.LoginRequestDTO;
import com.projetoa3.medsync.dtos.UpdateUserDTO;
import com.projetoa3.medsync.exceptions.EmailAlreadyExistsException;
import com.projetoa3.medsync.exceptions.InvalidCredentialsException;
import com.projetoa3.medsync.infra.jwt.JwtUtil;
import com.projetoa3.medsync.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            userService.save(user);
            return ResponseEntity.ok("Usuário cadastrado com sucesso");
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            User user = userService.authenticate(loginRequest);
            String token = jwtUtil.generateToken(user.getId(), user.getEmail());
            return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody UpdateUserDTO user) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !(auth.getPrincipal() instanceof Long)) {
                return ResponseEntity.status(403).body("Usuário não autenticado.");
            }
            Long id = (Long) auth.getPrincipal();
            userService.update(id, user);
            return ResponseEntity.ok("Usuário atualizado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }
}
