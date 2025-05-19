package com.example.educapp_proyecto.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="actividad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Actividad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_actividad")
    private Long idActividad;
    private String nombre;
    private String descripcion;
    private int duracion;

    @ManyToOne
    @JoinColumn(name = "solucion_aplicada_id")
    @JsonIgnore
    private SolucionAplicada solucionAplicada;

    private boolean completado;

    @ManyToMany(mappedBy = "actividades", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("actividades")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<PlanTrabajo> planesTrabajo = new HashSet<>();
}
