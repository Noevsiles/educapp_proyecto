package com.example.educapp_proyecto.model;

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

    @OneToMany(mappedBy = "solucion")
    private List<SolucionAplicada> solucionAplicadas;

}

