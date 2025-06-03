package com.projetoa3.medsync.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ConsultaResponseDTO {
    private Long id;
    private String nomeMedico;
    private String especialidade;
    private LocalDate data;
    private LocalTime hora;
    private Boolean ehRecorrente;
    private String observacoes;
}
