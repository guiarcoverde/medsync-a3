package com.projetoa3.medsync.repositories;

import com.projetoa3.medsync.domain.consulta.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByUsuarioId(Long usuarioId);
}
