package com.example.educapp_proyecto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="causa_de_problema")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CausaDeProblema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_causa_de_problema")
    private Long idCausaDeProblema;

    @ManyToOne
    @JoinColumn(name = "problema_id")
    private ProblemaDeConducta problemaDeConducta;

    @ManyToOne
    @JoinColumn(name = "causa_id")
    private Causa causa;
}

