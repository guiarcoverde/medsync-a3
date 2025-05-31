package com.projetoa3.medsync.controllers;

import com.projetoa3.medsync.domain.consulta.Consulta;
import com.projetoa3.medsync.dtos.CreateConsultaDTO;
import com.projetoa3.medsync.services.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @PostMapping
    public ResponseEntity<?> createConsulta(@RequestBody CreateConsultaDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Long)) {
            return ResponseEntity.status(403).body("Usuário não autenticado.");
        }
        Long userId = (Long) auth.getPrincipal();
        Consulta consulta = consultaService.createConsulta(userId, dto);
        return ResponseEntity.ok(consulta);
    }

    @GetMapping
    public ResponseEntity<?> getConsultas() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Long)) {
            return ResponseEntity.status(403).body("Usuário não autenticado.");
        }
        Long userId = (Long) auth.getPrincipal();
        List<Consulta> consultas = consultaService.getConsultasByUserId(userId);
        return ResponseEntity.ok(consultas);
    }
}
