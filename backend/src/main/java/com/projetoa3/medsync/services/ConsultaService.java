package com.projetoa3.medsync.services;

import com.projetoa3.medsync.domain.consulta.Consulta;
import com.projetoa3.medsync.domain.user.User;
import com.projetoa3.medsync.dtos.CreateConsultaDTO;
import com.projetoa3.medsync.repositories.ConsultaRepository;
import com.projetoa3.medsync.repositories.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private UserRepository userRepository;

    public Consulta createConsulta(Long userId, CreateConsultaDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Consulta consulta = new Consulta();
        consulta.setUsuario(user);
        consulta.setNomeMedico(dto.getNomeMedico());
        consulta.setEspecialidade(dto.getEspecialidade());
        consulta.setData(dto.getData());
        consulta.setHora(dto.getHora());
        consulta.setEhRecorrente(dto.getEhRecorrente());
        consulta.setObservacoes(dto.getObservacoes());

        return consultaRepository.save(consulta);
    }

    public List<Consulta> getConsultasByUserId(Long userId) {
        List<Consulta> consultas = consultaRepository.findByUsuarioId(userId);
        consultas.forEach(consulta -> Hibernate.initialize(consulta.getUsuario()));
        return consultas;
    }

    public Consulta updateConsultaDateAndHour(Long consultaId, Long userId, LocalDate data, LocalTime hora) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        if (!consulta.getUsuario().getId().equals(userId)) {
            throw new RuntimeException("Usuário não autorizado a atualizar esta consulta");
        }
        consulta.setData(data);
        consulta.setHora(hora);
        return consultaRepository.save(consulta);
    }
}
