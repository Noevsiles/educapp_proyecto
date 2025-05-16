package com.example.educapp_proyecto.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="sesion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_sesion")
    private Long idSesion;

    private LocalDateTime fechaHora;
    private String tipoSesion;

    private boolean realizada = false;
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "educador_id")
    private Educador educador;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "perro_id")
    private Perro perro;

    @ManyToOne
    @JoinColumn(name = "plan_trabajo_id")
    private PlanTrabajo planTrabajo;

}
