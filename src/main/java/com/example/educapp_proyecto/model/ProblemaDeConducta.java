package com.example.educapp_proyecto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name="problema_de_conducta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemaDeConducta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_problema_conducta")
    private Long idProblema;
    private String nombre;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "perro_id")
    private Perro perro;

    @OneToMany(mappedBy = "problemaDeConducta")
    private List<SolucionAplicada> solucionAplicadas;

    @OneToMany(mappedBy = "problemaDeConducta")
    private List<CausaDeProblema> causaDeProblemas;

}
