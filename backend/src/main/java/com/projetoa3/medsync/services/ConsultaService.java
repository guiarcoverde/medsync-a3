package com.projetoa3.medsync.services;

import com.projetoa3.medsync.domain.consulta.Consulta;
import com.projetoa3.medsync.domain.user.User;
import com.projetoa3.medsync.dtos.CreateConsultaDTO;
import com.projetoa3.medsync.repositories.ConsultaRepository;
import com.projetoa3.medsync.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return consultaRepository.findByUsuarioId(userId);
    }
}
