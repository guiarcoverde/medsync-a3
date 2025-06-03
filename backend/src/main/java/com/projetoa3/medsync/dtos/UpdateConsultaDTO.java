package com.projetoa3.medsync.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class UpdateConsultaDTO {
    private LocalDate data;
    private LocalTime hora;
}
