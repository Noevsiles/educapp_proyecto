package com.example.educapp_proyecto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "disponibilidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "educador_id", nullable = false)
    private Educador educador;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", length = 20)
    private DiaSemana diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
