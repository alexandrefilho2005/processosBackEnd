package com.entrevista.processos.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Processo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String npu;

    @Column(nullable = false)
    private LocalDate dataCadastro;

    private LocalDate dataVisualizacao;

    @Column(nullable = false)
    private String municipio;

    @Column(nullable = false)
    private String uf;

    @Lob
    @Column(nullable = false)
    private byte[] documento;

}
