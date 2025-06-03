package com.projetoa3.medsync.domain.consultarecorrente;


import com.projetoa3.medsync.domain.consulta.Consulta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "consultas_recorrentes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaRecorrente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento ManyToOne com Consulta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id", nullable = false)
    private Consulta consulta;

    @Column(name = "tipo_recorrencia")
    private String tipoRecorrencia;

    // intervalo em dias, por exemplo
    private Integer intervalo;

    @Column(name = "data_fim")
    private LocalDate dataFim;
}