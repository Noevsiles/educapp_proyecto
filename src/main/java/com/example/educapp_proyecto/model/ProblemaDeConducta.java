package com.example.educapp_proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perro_id")
    private Perro perro;

    @OneToMany(mappedBy = "problemaDeConducta")
    private List<SolucionAplicada> solucionAplicadas = new ArrayList<>();

    @OneToMany(mappedBy = "problemaDeConducta")
    private List<CausaDeProblema> causaDeProblemas = new ArrayList<>();

    @ManyToMany(mappedBy = "problemasDeConducta")
    private List<Perro> perros = new ArrayList<>();

}
