package com.projetoa3.medsync.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateUserDTO {

    private BigDecimal alturaCm;

    private BigDecimal pesoKg;

    private String doencasCronicas;

    private String alergias;

    private String medicamentos;

    private String contatoEmergenciaNome;

    private String contatoEmergenciaTelefone;
}