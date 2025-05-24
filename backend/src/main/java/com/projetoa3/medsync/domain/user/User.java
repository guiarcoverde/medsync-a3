package com.projetoa3.medsync.domain.user;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    @Column(name = "senha_hash")
    private String senhaHash;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    private String genero;

    @Column(name = "tipo_sanguineo")
    private String tipoSanguineo;

    @Column(name = "altura_cm")
    private BigDecimal alturaCm;

    @Column(name = "peso_kg")
    private BigDecimal pesoKg;

    @Column(name = "doencas_cronicas", columnDefinition = "TEXT")
    private String doencasCronicas;

    @Column(columnDefinition = "TEXT")
    private String alergias;

    @Column(columnDefinition = "TEXT")
    private String medicamentos;

    @Column(name = "contato_emergencia_nome")
    private String contatoEmergenciaNome;

    @Column(name = "contato_emergencia_telefone")
    private String contatoEmergenciaTelefone;

    @Column(name = "criado_em", updatable = false)
    @CreationTimestamp
    private LocalDateTime criadoEm;
}
