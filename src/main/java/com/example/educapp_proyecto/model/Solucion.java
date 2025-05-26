package com.example.educapp_proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="solucion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Solucion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_solucion")
    private Long idSolucion;
    private String nombre;
    private String descripcion;
    private String categoria;
    private String dificultad;
    private String requerimientos;
    private String notas;

    @JsonIgnore
    @OneToMany(mappedBy = "solucion")
    private List<SolucionAplicada> solucionAplicadas;

    @ManyToOne
    @JoinColumn(name = "problema_id")
    private ProblemaDeConducta problemaDeConducta;


}

