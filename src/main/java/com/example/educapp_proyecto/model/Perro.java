package com.example.educapp_proyecto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="perro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Perro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_perro")
    private Long idPerro;

    private String nombre;
    private String raza;
    private String sexo;
    private Integer edad;
    private boolean esterilizado;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "perro")
    private List<ProblemaDeConducta> problemasDeConducta;
}
