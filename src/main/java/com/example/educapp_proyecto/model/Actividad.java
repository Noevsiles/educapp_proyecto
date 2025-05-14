package com.example.educapp_proyecto.model;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private SolucionAplicada solucionAplicada;

    private boolean completado;

}
