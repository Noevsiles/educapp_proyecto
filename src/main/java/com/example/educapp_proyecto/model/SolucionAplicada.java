package com.example.educapp_proyecto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="solucion_aplicada")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolucionAplicada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_solucion_aplicada")
    private Long idSolucionAplicada;

    private Integer numeroDeSesiones;

    @ManyToOne
    @JoinColumn(name = "id_problema_de_conducta")
    private ProblemaDeConducta problemaDeConducta;

    @ManyToOne
    @JoinColumn(name = "id_solucion")
    private Solucion solucion;

    @OneToMany(mappedBy = "solucionAplicada", cascade = CascadeType.ALL)
    private List<Actividad> actividades;

}
