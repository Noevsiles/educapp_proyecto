package com.example.educapp_proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plan_trabajo")
public class PlanTrabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;

    // Relación con Cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", referencedColumnName = "id_cliente")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "perros", "educador"})
    private Cliente cliente;

    // Relación muchos a muchos con Problemas de conducta
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "plan_trabajo_problemas",
            joinColumns = @JoinColumn(name = "plan_trabajo_id"),
            inverseJoinColumns = @JoinColumn(name = "problemas_id")
    )

    private Set<ProblemaDeConducta> problemas = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "plan_trabajo_actividades",
            joinColumns = @JoinColumn(name = "plan_trabajo_id"),
            inverseJoinColumns = @JoinColumn(name = "actividades_id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference
    private Set<Actividad> actividades = new HashSet<>();

    private String observaciones;

    //Relacion con perro
    @ManyToOne
    @JoinColumn(name = "perro_id")
    private Perro perro;

    private Integer numeroSesiones;

}

