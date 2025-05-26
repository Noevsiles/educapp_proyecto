package com.example.educapp_proyecto.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonBackReference
    private Cliente cliente;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "perro_problemas",
            joinColumns = @JoinColumn(name = "perro_id"),
            inverseJoinColumns = @JoinColumn(name = "problema_id")
    )
    private Set<ProblemaDeConducta> problemasDeConducta;
}
