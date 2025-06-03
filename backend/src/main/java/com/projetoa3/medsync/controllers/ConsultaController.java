package com.projetoa3.medsync.controllers;

import com.projetoa3.medsync.domain.consulta.Consulta;
import com.projetoa3.medsync.dtos.CreateConsultaDTO;
import com.projetoa3.medsync.dtos.UpdateConsultaDTO;
import com.projetoa3.medsync.dtos.ConsultaResponseDTO;
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

    @PostMapping("/create")
    public ResponseEntity<?> createConsulta(@RequestBody CreateConsultaDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Long)) {
            return ResponseEntity.status(403).body("Usuário não autenticado.");
        }
        Long userId = (Long) auth.getPrincipal();
        Consulta consulta = consultaService.createConsulta(userId, dto);
        return ResponseEntity.ok(consulta);
    }

    @GetMapping("/ver-consultas")
    public ResponseEntity<?> getConsultas() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Long)) {
            return ResponseEntity.status(403).body("Usuário não autenticado.");
        }
        Long userId = (Long) auth.getPrincipal();
        List<Consulta> consultas = consultaService.getConsultasByUserId(userId);
        List<ConsultaResponseDTO> response = consultas.stream().map(consulta -> {
            ConsultaResponseDTO dto = new ConsultaResponseDTO();
            dto.setId(consulta.getId());
            dto.setNomeMedico(consulta.getNomeMedico());
            dto.setEspecialidade(consulta.getEspecialidade());
            dto.setData(consulta.getData());
            dto.setHora(consulta.getHora());
            dto.setEhRecorrente(consulta.getEhRecorrente());
            dto.setObservacoes(consulta.getObservacoes());
            return dto;
        }).toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateConsulta(@PathVariable Long id, @RequestBody UpdateConsultaDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Long)) {
            return ResponseEntity.status(403).body("Usuário não autenticado.");
        }
        Long userId = (Long) auth.getPrincipal();
        try {
            Consulta updated = consultaService.updateConsultaDateAndHour(id, userId, dto.getData(), dto.getHora());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
